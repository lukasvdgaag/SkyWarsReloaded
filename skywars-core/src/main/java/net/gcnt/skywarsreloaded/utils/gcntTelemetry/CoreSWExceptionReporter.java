package net.gcnt.skywarsreloaded.utils.gcntTelemetry;

import com.google.gson.JsonObject;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoreSWExceptionReporter {

    private final SkyWarsReloaded plugin;

    public CoreSWExceptionReporter(final SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    public void reportException(Exception exception) {
        try {
            URL url = new URL("https://gcnt.net/api/swr/report/exception");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(false);
            String serializedException = this.filterException(exception);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("exception", serializedException);
            jsonObject.addProperty("plugin", "SkyWarsReloaded");
            jsonObject.addProperty("pluginVersion", this.plugin.getVersion());
            jsonObject.addProperty("serverVersion", this.plugin.getMinecraftVersion());
            jsonObject.addProperty("platformVersion", this.plugin.getPlatformVersion());

            connection.getOutputStream().write(jsonObject.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String filterException(Exception exception) {
        StringBuilder strb = new StringBuilder();

        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            if (stackTraceElement.getClassName().contains("net.gcnt.skywarsreloaded")) {
                strb.append(stackTraceElement.getClassName())
                        .append(".")
                        .append(stackTraceElement.getMethodName())
                        .append(" (")
                        .append(stackTraceElement.getFileName())
                        .append(":")
                        .append(stackTraceElement.getLineNumber())
                        .append(")")
                        .append("\n");
            }
        }

        return strb.toString();
    }

}
