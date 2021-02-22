/*
 * Copyright 2014 jamietech. All rights reserved.
 * https://github.com/jamietech/MinecraftServerPing
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */
package com.walrusone.skywarsreloaded.utilities.minecraftping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MinecraftPing {

    /**
     * Fetches a {@link MinecraftPingReply} for the supplied hostname.
     * <b>Assumed timeout of 2s and port of 25565.</b>
     *
     * @param hostname - a valid String hostname
     * @return {@link MinecraftPingReply}
     * @throws IOException If fail / cannot connect
     */
    public MinecraftPingReply getPing(final String hostname) throws IOException {
        return this.getPing(new MinecraftPingOptions().setHostname(hostname));
    }

    /**
     * Fetches a {@link MinecraftPingReply} for the supplied options.
     *
     * @param options - a filled instance of {@link MinecraftPingOptions}
     * @return {@link MinecraftPingReply}
     * @throws IOException If fail / cannot connect
     */
    public MinecraftPingReply getPing(final MinecraftPingOptions options) throws IOException {
        MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
        // TODO: Useless check: int is never null! -- MinecraftPingUtil.validate(options.getPort(), "Port cannot be null.");


        if (SkyWarsReloaded.getCfg().debugEnabled()) { SkyWarsReloaded.get().getLogger().warning(
                "Ping setup socket " + options.getHostname() + " " + options.getPort() + " " + options.getTimeout()); }

        final Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());
        } catch (ConnectException e) {
            SkyWarsReloaded.get().getLogger().warning(
                    "Failed to ping server " +
                            options.getHostname() + ":" + options.getPort() +
                            " (" + e.getMessage() + ")"
            );
        }

        final DataInputStream in = new DataInputStream(socket.getInputStream());
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        //> Handshake

        ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshake_bytes);

        handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE);
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION);
        MinecraftPingUtil.writeVarInt(handshake, options.getHostname().length());
        handshake.writeBytes(options.getHostname());
        handshake.writeShort(options.getPort());
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE);

        MinecraftPingUtil.writeVarInt(out, handshake_bytes.size());
        out.write(handshake_bytes.toByteArray());

        if (SkyWarsReloaded.getCfg().debugEnabled()) { SkyWarsReloaded.get().getLogger().warning(
                "Ping send " + options.getHostname() + " " + options.getPort() + " " + options.getTimeout()); }

        //> Status request

        out.writeByte(0x01); // Size of packet
        out.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST);

        //< Status response

        if (SkyWarsReloaded.getCfg().debugEnabled()) { SkyWarsReloaded.get().getLogger().warning(
                "Ping start receive " + options.getHostname() + " " + options.getPort() + " " + options.getTimeout()); }

        MinecraftPingUtil.readVarInt(in); // Size
        int id = MinecraftPingUtil.readVarInt(in);

        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_STATUSREQUEST, "Server returned invalid packet.");

        int length = MinecraftPingUtil.readVarInt(in);
        MinecraftPingUtil.io(length == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(length == 0, "Server returned unexpected value.");

        byte[] data = new byte[length];
        in.readFully(data);

        if (SkyWarsReloaded.getCfg().debugEnabled()) { SkyWarsReloaded.get().getLogger().warning(
                "Ping done " + options.getHostname() + " " + options.getPort() + " " + options.getTimeout()); }

        String json = new String(data, options.getCharset());

        //> Ping

        out.writeByte(0x09); // Size of packet
        out.writeByte(MinecraftPingUtil.PACKET_PING);
        out.writeLong(System.currentTimeMillis());

        //< Ping

        MinecraftPingUtil.readVarInt(in); // Size
        id = MinecraftPingUtil.readVarInt(in);
        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_PING, "Server returned invalid packet.");

        // Close

        handshake.close();
        handshake_bytes.close();
        out.close();
        in.close();
        socket.close();

        // DEBUG: Example json response -
        // 1.8 description is the motd
        // 1.12 description is obj that contains text field which is motd
        // 1.13+ desc is obj, which contains extra field, which is an array which contains, at pos 0, obj which contains text field, which is motd
        // 1.16 example: {"description":{"extra":[{"text":"WAITINGSTART:0:8:Medieval #1"}],"text":""},"players":{"max":20,"online":1},"version":{"name":"Spigot 1.16.4","protocol":754}}
        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            SkyWarsReloaded.get().getLogger().info("JSON Ping Reply: " + json);
        }

        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();

        // Get Ping Version
        int replyProtocolVersion = obj.getAsJsonObject("version").get("protocol").getAsInt();
        MinecraftPingReply.Version pingVersion = new MinecraftPingReply.Version(
                obj.getAsJsonObject("version").get("name").getAsString(),
                replyProtocolVersion);

        // Get Ping Players
        MinecraftPingReply.Players pingPlayers = new MinecraftPingReply.Players(
                obj.getAsJsonObject("players").get("max").getAsInt(),
                obj.getAsJsonObject("players").get("online").getAsInt());

        // Get Ping Description for correct protocol version
        MinecraftPingReply.Description pingDescription =
                new MinecraftPingReply.Description(getMotdTextFromJsonResponse(obj));

        // Put all together
        return new MinecraftPingReply(pingDescription, pingPlayers, pingVersion, "");

       // return new Gson().fromJson(json, MinecraftPingReply.class);
    }

    private String getMotdTextFromJsonResponse(JsonObject obj) {
        JsonElement jsonElement = obj.get("description");
        // 1.8
        if (jsonElement instanceof JsonPrimitive)  return jsonElement.getAsString();
        // 1.12
        JsonObject descJsonObj = jsonElement.getAsJsonObject();
        if (descJsonObj.has("extra")) jsonElement = descJsonObj.get("extra"); // 1.13+
        else if (descJsonObj.has("text")) jsonElement = descJsonObj.get("text"); // 1.12
        if (jsonElement instanceof JsonPrimitive)  return jsonElement.getAsString(); // will be false in 1.13+
        // 1.13+
        jsonElement = jsonElement.getAsJsonArray().get(0);
        if (jsonElement instanceof JsonPrimitive)  return jsonElement.getAsString();
        jsonElement = jsonElement.getAsJsonObject().get("text");
        if (jsonElement instanceof JsonPrimitive)  return jsonElement.getAsString();
        return "";
    }

}
