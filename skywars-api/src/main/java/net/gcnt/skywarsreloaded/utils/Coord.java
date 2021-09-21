package net.gcnt.skywarsreloaded.utils;

public record Coord(int x, int y, int z) {

    /**
     * Get a Coord loc from a formatted input string (format: x:y:z)
     *
     * @param input Formatted coord loc string.
     * @return Coord instance with information from input string.
     * @throws IndexOutOfBoundsException Thrown if there are more/less than 3 points found in this formatted input string.
     * @throws NumberFormatException     Thrown if one of the points seems to not be an integer.
     */
    public static Coord fromString(String input) throws IndexOutOfBoundsException, NumberFormatException {
        String[] arg0 = input.split(":");
        if (arg0.length != 3) {
            throw new IndexOutOfBoundsException("The coord input string \"" + input + "\" has an invalid amount of arguments.");
        } else if (!SWUtils.isInt(arg0[0]) || !SWUtils.isInt(arg0[1]) || !SWUtils.isInt(arg0[2])) {
            throw new NumberFormatException("One of the coord points seems to not be an integer: " + input);
        }

        return new Coord(Integer.parseInt(arg0[0]), Integer.parseInt(arg0[1]), Integer.parseInt(arg0[2]));
    }

    @Override
    public String toString() {
        return x + ":" + y + ":" + z;
    }

    public boolean equals(Coord o) {
        return (o.toString().equals(this.toString()));
    }
}
