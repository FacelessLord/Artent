package faceless.artent.transmutations.gui;

import net.minecraft.util.Identifier;

import java.util.Hashtable;

import faceless.artent.Artent;

public enum PartType {
    CircleBig("C1", "circle_big"),
    CircleMedium("C2", "circle_medium"),
    CircleSmall("C3", "circle_small"),
    FlowerBig("F1", "flower_big"),
    FlowerMedium("F2", "flower_medium"),
    FlowerSmall("F3", "flower_small"),
    HexBig("H1", "hex_big", "hex_rev_big"),
    HexMedium("H2", "hex_medium", "hex_rev_medium"),
    HexSmall("H3", "hex_small", "hex_rev_small"),
    LightMoonBig("L1", "light_big", "moon_big"),
    LightMoonMedium("L2", "light_medium", "moon_medium"),
    LightMoonSmall("L3", "light_small", "moon_small"),
    PentagonBig("P1", "pentagon_big", "pentagon_rev_big"),
    PentagonMedium("P2", "pentagon_medium", "pentagon_rev_medium"),
    PentagonSmall("P3", "pentagon_small", "pentagon_rev_small"),
    PentagramBig("P4", "pentagram_big", "pentagram_rev_big"),
    PentagramMedium("P5", "pentagram_medium", "pentagram_rev_medium"),
    PentagramSmall("P6", "pentagram_small", "pentagram_rev_small"),
    RectangleBig("R1", "rect_big", "rhomb_big"),
    RectangleMedium("R2", "rect_medium", "rhomb_medium"),
    RectangleSmall("R3", "rect_small", "rhomb_small"),
    ShieldTreeBig("D1", "shield_big", "tree_big"),
    ShieldTreeMedium("D2", "shield_medium", "tree_medium"),
    ShieldTreeSmall("D3", "shield_small", "tree_small"),
    TriangleBig("T1", "triangle_big", "triangle_rev_big"),
    TriangleMedium("T2", "triangle_medium", "triangle_rev_medium"),
    TriangleSmall("T3", "triangle_small", "triangle_rev_small"),
    TrianglesBig("S1", "triangles_big", "triangles_rev_big"),
    TrianglesMedium("S2", "triangles_medium", "triangles_rev_medium"),
    TrianglesSmall("S3", "triangles_small", "triangles_rev_small");

    public static final String blockPathBase = "textures/block/circles/part_";
    public static final String modelPathBase = "models/block/circles/";
    public static final String itemPathBase = "textures/item/chalk/paper_";

    public final String codeString;
    public final Identifier itemTexture;
    public final Identifier blockTexture;
    public final Identifier itemTextureRev;
    public final Identifier blockTextureRev;
    public final String circlePartModel;
    public final String circlePartModelRev;
    public final int id;

    PartType(String codeString, String textureId) {
        this.codeString = codeString;
        this.itemTextureRev = this.itemTexture = new Identifier(Artent.MODID, itemPathBase + textureId + ".png");
        this.blockTextureRev = this.blockTexture = new Identifier(Artent.MODID, blockPathBase + textureId + ".png");
        this.circlePartModelRev = this.circlePartModel = modelPathBase + textureId + ".json";
        PartTypes.reverseMapping.put(codeString, this);
        this.id = PartTypes.PartId++;
    }

    PartType(String codeString, String textureId, String reversedTextureId) {
        this.codeString = codeString;
        this.itemTexture = new Identifier(Artent.MODID, itemPathBase + textureId + ".png");
        this.blockTexture = new Identifier(Artent.MODID, blockPathBase + textureId + ".png");
        this.circlePartModel = modelPathBase + textureId + ".json";

        this.itemTextureRev = new Identifier(Artent.MODID, itemPathBase + reversedTextureId + ".png");
        this.blockTextureRev = new Identifier(Artent.MODID, blockPathBase + reversedTextureId + ".png");
        this.circlePartModelRev = modelPathBase + reversedTextureId + ".json";

        PartTypes.reverseMapping.put(codeString, this);
        this.id = PartTypes.PartId;
        PartTypes.PartId += 2;
    }

    public static PartType fromCode(String codeString) {
        return PartTypes.reverseMapping.get(codeString);
    }

    @Override
    public String toString() {
        return codeString;
    }

    private static class PartTypes {
        private static final Hashtable<String, PartType> reverseMapping = new Hashtable<>();
        public static int PartId = 1;
    }
}
