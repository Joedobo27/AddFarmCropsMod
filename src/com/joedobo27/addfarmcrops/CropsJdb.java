package com.joedobo27.addfarmcrops;

import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTemplateFactory;
import com.wurmonline.server.items.NoSuchTemplateException;

import java.util.Arrays;
import java.util.Objects;

import static com.wurmonline.server.items.ItemList.*;

public enum CropsJdb {
    EMPTY(-1, "empty", -1, -1, "empty", -1),
    BARLEY(0, "barley", 28, 28, "handfuls", 20),
    WHEAT(1, "wheat", 29, 29, "handfuls", 30),
    RYE(2, "rye", 30, 30, "handfuls", 10),
    OAT(3, "oat", 31, 31, "handfuls", 15),
    CORN(4, "corn", 32, 32, "stalks", 40),
    PUMPKIN(5, "pumpkin", 34, 33, "", 15),
    POTATO(6, "potato", 35, 35, "", 4),
    COTTON(7, "cotton", cottonSeed, cotton, "bales", 7),
    WEMP(8, "wemp", 317, 316, "bales", 10),
    GARLIC(9, "garlic", 356, 356, "bunch", 70),
    ONION(10, "onion", 355, 355, "bunch", 60),
    REED(11, "reed", 744, 743, "bales", 20),
    RICE(12, "rice", 746, 746, "handfuls", 80),
    STRAWBERRIES(13, "strawberries", 750, 362, "handfuls", 60),
    CARROTS(14, "carrots", 1145, 1133, "handfuls", 25),
    CABBAGE(15, "cabbage", 1146, 1134, "", 35),
    TOMATOS(16, "tomatos", 1147, 1135, "handfuls", 45),
    SUGAR_BEET(17, "sugar beet", 1148, 1136, "", 85),
    LETTUCE(18, "lettuce", 1149, 1137, "", 55),
    PEAS(19, "peas", 1150, 1138, "handfuls", 65),
    CUCUMBER(20, "cucumber", 1248, 1247, "", 15),
    BASIL(21, "basil", basil, basil, "handfuls", 10),
    BELLADONNA(22, "belladonna", belladonna, belladonna, "handfuls", 10),
    LOVAGE(23, "lovage", lovage, lovage, "handfuls", 10),
    NETTLES(24, "nettles", nettles, nettles, "handfuls", 10),
    OREGANO(25, "oregano", oregano, oregano, "handfuls", 10),
    PARSLEY(26, "parsley", parsley, parsley, "handfuls", 10),
    ROSEMARY(27, "rosemary", rosemary, rosemary, "handfuls", 10),
    SAGE(28, "sage", sage, sage, "handfuls", 10),
    SASSAFRAS(29, "sassafras", sassafras, sassafras, "handfuls", 10),
    THYME(30, "thyme", thyme, thyme, "handfuls", 10),
    FENNEL(31, "fennel", fennelSeeds, fennel, "bunch", 10),
    MINT(32, "mint", mint, mint, "handfuls", 10),
    CUMIN(33, "cumin", cumin, cumin, "handfuls", 10),
    GINGER(34, "ginger", ginger, ginger, "handfuls", 10),
    NUTMEG(35, "nutmeg", nutmeg, nutmeg, "handfuls", 10),
    PAPRIKA(36, "paprika", paprikaSeeds, paprika, "handfuls", 10),
    TURMERIC(37, "turmeric", turmericSeeds, turmeric, "handfuls", 10),
    BLUEBERRY(38, "blueberry", blueberry, blueberry, "handfuls", 60),
    LINGONBERRY(39, "lingonberry", lingonberry, lingonberry, "handfuls", 20),
    RASPBERRY(40, "raspberries", raspberries, raspberries, "handfuls", 20),
    COCOABEAN(41, "cocoa bean", cocoaBean, cocoaBean, "handfuls", 85),
    WOAD(42, "woad", woad, woad, "handfuls", 90),
    BLACK_MUSHROOM(43, "black mushroom", mushroomBlack, mushroomBlack, "", 90),
    BLUE_MUSHROOM(44, "blue mushroom", mushroomBlue, mushroomBlue, "", 70),
    BROWN_MUSHROOM(45, "brown mushroom", mushroomBrown, mushroomBrown, "", 60),
    GREEN_MUSHROOM(46, "green mushroom", mushroomGreen, mushroomGreen, "", 80),
    RED_MUSHROOM(47, "red mushroom", mushroomRed, mushroomRed, "", 20),
    YELLOW_MUSHROOM(48, "yellow mushroom", mushroomYellow, mushroomYellow, "", 70),
    PINE_NUT(49, "pine nut", pineNuts, pineNuts, "handfuls", 50),
    WALNUT(50, "walnut", walnut, walnut, "handfuls", 50),
    HAZELNUT(51, "hazelnut", nutHazel, nutHazel, "handfuls", 50),
    CHESTNUT(52, "chestnut", chestnut, chestnut, "handfuls", 50),
    HOPS(53, "hops", hops, hops, "handfuls", 70),
    BLUE_GRAPE(54, "blue grapes", grapesBlue, grapesBlue, "handfuls", 70),
    GREEN_GRAPE(55, "green grapes", grapesGreen, grapesGreen, "handfuls", 70),
    OLIVE(56, "olive", olive, olive, "handfuls", 50),
    ALMOND(57, "almond", AddFarmCropsMod.almond, AddFarmCropsMod.almond, "handfulls", 50),
    PEANUT(58, "peanut", AddFarmCropsMod.peanut, AddFarmCropsMod.peanut, "handfulls", 50),
    WAX_GOURD(59, "gourd canteen", AddFarmCropsMod.gourdCanteen, AddFarmCropsMod.gourdCanteen, "", 50),
    MADDER(60, "madder", AddFarmCropsMod.madder, AddFarmCropsMod.madder, "handfulls", 50);

    private int id;
    private final String name;
    private final int seedTemplateId;
    private final int productTemplateId;
    private final String measure;
    private final double difficulty;
    private static CropsJdb instance;

    CropsJdb(int id, String name, int seedTemplateId, int productTemplateId, String measure, double difficulty) {

        this.id = id;
        this.name = name;
        this.seedTemplateId = seedTemplateId;
        this.productTemplateId = productTemplateId;
        this.measure = measure;
        this.difficulty = difficulty;
    }

    static void setEmptyId(int i) {
        EMPTY.id = i;
    }

    public static int getSeedGramsFromCropId(int cropId) throws NoSuchTemplateException {
        CropsJdb crops = Arrays.stream(values())
                .filter(crops1 -> crops1.id == cropId)
                .findFirst()
                .orElse(CropsJdb.EMPTY);
        if (crops.id >= AddFarmCropsMod.getLastUsableEntry())
            crops = CropsJdb.EMPTY;
        return ItemTemplateFactory.getInstance().getTemplate(crops.seedTemplateId).getWeightGrams();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public int getSeedTemplateId() {
        return this.seedTemplateId;
    }

    public int getProductTemplateId() {return this.productTemplateId;}

    public String getMeasure() {
        return measure;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public static CropsJdb getCrop(int cropId){
        return Arrays.stream(values())
                .filter(crops1 -> crops1.id == cropId)
                .findFirst()
                .orElse(CropsJdb.EMPTY);
    }

    public static String getCropNameFromCropId(int cropId, boolean isSeed) throws NoSuchTemplateException {
        CropsJdb crops = Arrays.stream(values())
                .filter(crops1 -> crops1.id == cropId)
                .findFirst()
                .orElse(CropsJdb.EMPTY);
        if (crops.id >= AddFarmCropsMod.getLastUsableEntry())
            crops = CropsJdb.EMPTY;
        ItemTemplate cropTemplate = null;
        ItemTemplate seedTemplate = null;

        cropTemplate = ItemTemplateFactory.getInstance().getTemplate(CropsJdb.getProductTemplateIdFromCropId(crops.id));
        seedTemplate = ItemTemplateFactory.getInstance().getTemplate(CropsJdb.getSeedTemplateIdFromCropId(crops.id));

        if (isSeed) {
            return seedTemplate.getName();
        }
        return cropTemplate.getName();
    }

    static ItemTemplate getSeedTemplateFromProductTemplate(ItemTemplate productTemplate) throws NoSuchTemplateException, CropsException {
        if (productTemplate.getTemplateId() == ItemList.bulkItem)
            throw new CropsException("arg requires getRealTemplate() preparation.");
        CropsJdb crop = Arrays.stream(values())
                .filter(crops -> crops.productTemplateId == productTemplate.getTemplateId())
                .findFirst()
                .orElseThrow(() -> new CropsException("No matching productTemplateId found."));
        if (crop.id >= AddFarmCropsMod.getLastUsableEntry())
            throw new CropsException("Crops.id found isn't supported.");
        return ItemTemplateFactory.getInstance().getTemplate(crop.getSeedTemplateId());
    }

    static int getSeedTemplateIdFromCropId(int cropId) {
        CropsJdb crop = Arrays.stream(values())
                .filter(crops -> crops.id == cropId)
                .findFirst()
                .orElse(CropsJdb.EMPTY);
        if (crop.id >= AddFarmCropsMod.getLastUsableEntry())
            crop = CropsJdb.EMPTY;
        return crop.seedTemplateId;
    }

    static ItemTemplate getSeedTemplateFromCropId(int cropId) throws NoSuchTemplateException, CropsException {
        CropsJdb crop = Arrays.stream(values())
                .filter(crops -> crops.id == cropId)
                .findFirst()
                .orElseThrow(() -> new CropsException("cropId isn't valid so no ItemTemplate can be found."));
        if (crop.id >= AddFarmCropsMod.getLastUsableEntry())
            throw new CropsException("Crops.id found isn't supported.");
        return ItemTemplateFactory.getInstance().getTemplate(crop.getSeedTemplateId());
    }

    static int getProductTemplateIdFromCropId(int cropId) {
        CropsJdb crop = Arrays.stream(values())
                .filter(crops -> crops.id == cropId)
                .findFirst()
                .orElse(CropsJdb.EMPTY);
        if (crop.id >= AddFarmCropsMod.getLastUsableEntry())
            crop = CropsJdb.EMPTY;
        return crop.productTemplateId;
    }

    static ItemTemplate getProductTemplateFromCropId(int cropId) throws NoSuchTemplateException, CropsException {
        CropsJdb crop = Arrays.stream(values())
                .filter(crops -> crops.id == cropId)
                .findFirst()
                .orElseThrow(() -> new CropsException("cropId isn't valid so no ItemTemplate can be found."));
        if (crop.id >= AddFarmCropsMod.getLastUsableEntry())
            throw new CropsException("Crops.id found isn't supported.");
        return ItemTemplateFactory.getInstance().getTemplate(crop.getProductTemplateId());
    }

    static int getCropIdFromSeedTemplateId(int aSeedTemplateId) {
        int id = Arrays.stream(values())
                .filter(crops -> Objects.equals(crops.seedTemplateId, aSeedTemplateId))
                .mapToInt(crops -> crops.id)
                .findFirst()
                .orElse(AddFarmCropsMod.getLastUsableEntry());
        if (id > AddFarmCropsMod.getLastUsableEntry())
            return AddFarmCropsMod.getLastUsableEntry();
        return id;
    }

    static int getCropIdFromProductTemplateId(int aProductTemplateId) {
        int id = Arrays.stream(values())
                .filter(crops -> Objects.equals(crops.productTemplateId, aProductTemplateId))
                .mapToInt(crops -> crops.id)
                .findFirst()
                .orElse(AddFarmCropsMod.getLastUsableEntry());
        if (id > AddFarmCropsMod.getLastUsableEntry())
            return AddFarmCropsMod.getLastUsableEntry();
        return id;
    }

    static double getCropDifficultyFromCropId(int cropId) {
        CropsJdb crops = Arrays.stream(values())
                .filter(crop -> Objects.equals(cropId, crop.id))
                .findFirst()
                .orElse(CropsJdb.EMPTY);
        if (crops == EMPTY || crops.id >= AddFarmCropsMod.getLastUsableEntry())
            return -1;
        return crops.difficulty;
    }
}
