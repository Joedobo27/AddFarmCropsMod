package com.joedobo27.addfarmcrops;



import com.wurmonline.server.behaviours.Crops;
import com.wurmonline.server.items.ItemTemplateFactory;
import com.wurmonline.server.items.ItemTypes;
import javassist.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.IdFactory;
import org.gotti.wurmunlimited.modsupport.IdType;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddFarmCropsMod implements WurmServerMod, PreInitable, ItemTemplatesCreatedListener,
        Configurable {

    private static Logger logger = Logger.getLogger(AddFarmCropsMod.class.getName());
    private static int lastUsableEntry = 63;
    static int almond;
    static int peanut;
    static int madder;
    static int gourdCanteen;
    public static Crops[] cropTypes;
    private static final String[] STEAM_VERSION = new String[]{"1.3.1.3"};
    private static boolean versionCompliant = false;

    @Override
    public void configure(Properties properties) {

        lastUsableEntry = Integer.parseInt(properties.getProperty("lastUsableEntry", Integer.toString(lastUsableEntry)));
        if (Arrays.stream(STEAM_VERSION)
                .filter(s -> Objects.equals(s, properties.getProperty("steamVersion", null)))
                .count() > 0)
            versionCompliant = true;
        else
            logger.log(Level.WARNING, "WU version mismatch. Your " + properties.getProperty(" steamVersion", null)
                    + "version doesn't match one of BulkOptionsMod's required versions " + Arrays.toString(STEAM_VERSION));
    }

    @Override
    public void preInit() {
        if (!versionCompliant)
            return;
        try {
            addNewFarmTiles();
            overwriteGetTileType();
            cropTypesCodeConvert();
        }catch (NotFoundException | CannotCompileException | BadBytecode | ClassNotFoundException e){
            logger.warning(e.getMessage());
        }


        InvocationHandlerFactory invocationHandlerFactory = () -> (wrapped, method, args) -> {
            cropTypes = Arrays.stream(CropsJdb.values())
                    .map(cropsJdb -> invokeCropsWrapper(cropsJdb.getId(), cropsJdb.getName(), cropsJdb.getSeedTemplateId(), cropsJdb.getProductTemplateId(),
                            cropsJdb.getMeasure(), cropsJdb.getDifficulty()))
                    .filter(Objects::nonNull)
                    .toArray(Crops[]::new);
            CropsJdb.setEmptyId(lastUsableEntry);
            Object result = method.invoke(wrapped, args);
            return result;
        };
        HookManager.getInstance().registerHook("com.wurmonline.server.zones.CropTilePoller", "initializeFields",
                "()V", invocationHandlerFactory);
    }


    @Override
    public void onItemTemplatesCreated() {
        if (!versionCompliant)
            return;
        addSeedFlags();
        addNewCropTemplates();
    }

    private static void addNewCropTemplates() {
        ItemTemplateBuilder jdbAlmond = new ItemTemplateBuilder("jdbAlmond");
        almond = IdFactory.getIdFor("jdbAlmond", IdType.ITEMTEMPLATE);
        jdbAlmond.name("almond","almonds", "A sweet tasting and saucer-like-oval shaped nut.");
        jdbAlmond.size(3);
        //jdbAlmond.descriptions();
        jdbAlmond.itemTypes(new short[]{ItemTypes.ITEM_TYPE_WOOD, ItemTypes.ITEM_TYPE_NAMED, ItemTypes.ITEM_TYPE_REPAIRABLE,
                ItemTypes.ITEM_TYPE_COLORABLE, ItemTypes.ITEM_TYPE_HASDATA});
        jdbAlmond.imageNumber((short) 245);
        jdbAlmond.behaviourType((short) 1);
        jdbAlmond.combatDamage(0);
        jdbAlmond.decayTime(2419200L);
        jdbAlmond.dimensions(30, 30, 50);
        jdbAlmond.primarySkill(-10);
        //jdbAlmond.bodySpaces();
        jdbAlmond.modelName("model.container.barrel.small.");
        jdbAlmond.difficulty(5);
        jdbAlmond.weightGrams(1000);
        jdbAlmond.material((byte) 14);
        jdbAlmond.value(10000);
        jdbAlmond.isTraded(true);
        //jdbAlmond.armourType();
        try {
            jdbAlmond.build();
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }

        ItemTemplateBuilder jdbPeanut = new ItemTemplateBuilder("jdbPeanut");
        peanut = IdFactory.getIdFor("jdbPeanut", IdType.ITEMTEMPLATE);
        jdbPeanut.name("peanut","peanuts", "A oily cylindrical shaped bean.");
        jdbPeanut.size(3);
        //jdbPeanut.descriptions();
        jdbPeanut.itemTypes(new short[]{ItemTypes.ITEM_TYPE_WOOD, ItemTypes.ITEM_TYPE_NAMED, ItemTypes.ITEM_TYPE_REPAIRABLE,
                ItemTypes.ITEM_TYPE_COLORABLE, ItemTypes.ITEM_TYPE_HASDATA});
        jdbPeanut.imageNumber((short) 245);
        jdbPeanut.behaviourType((short) 1);
        jdbPeanut.combatDamage(0);
        jdbPeanut.decayTime(2419200L);
        jdbPeanut.dimensions(30, 30, 50);
        jdbPeanut.primarySkill(-10);
        //jdbPeanut.bodySpaces();
        jdbPeanut.modelName("model.container.barrel.small.");
        jdbPeanut.difficulty(5);
        jdbPeanut.weightGrams(1000);
        jdbPeanut.material((byte) 14);
        jdbPeanut.value(10000);
        jdbPeanut.isTraded(true);
        //jdbPeanut.armourType();
        try {
            jdbPeanut.build();
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage(), e);
        }

        ItemTemplateBuilder jdbMadder = new ItemTemplateBuilder("jdbMadder");
        madder = IdFactory.getIdFor("jdbMadder", IdType.ITEMTEMPLATE);
        jdbMadder.name("Madder", "Madders", "A plant with vibrant red roots.");
        jdbMadder.size(3);
        //jdbMadder.descriptions();
        jdbMadder.itemTypes(new short[]{46, 146, 164});
        jdbMadder.imageNumber((short) 711);
        jdbMadder.behaviourType((short) 1);
        jdbMadder.combatDamage(0);
        jdbMadder.decayTime(9072000L);
        jdbMadder.dimensions(3, 3, 3);
        jdbMadder.primarySkill(-10);
        //jdbMadder.bodySpaces();
        jdbMadder.modelName("model.herb.lovage.");
        jdbMadder.difficulty(100.0f);
        jdbMadder.weightGrams(50);
        jdbMadder.material((byte) 22);
        jdbMadder.value(100);
        jdbMadder.isTraded(true);
        //jdbMadder.armourType();
        try {
            jdbMadder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ItemTemplateBuilder jdbGourdCanteen = new ItemTemplateBuilder("jdbGourdCanteen");
        gourdCanteen = IdFactory.getIdFor("jdbGourdCanteen", IdType.ITEMTEMPLATE);
        jdbGourdCanteen.name("Gourd canteen", "Gourd canteens", "A hollowed out gourd for holding liquids.");
        jdbGourdCanteen.size(3);
        //jdbGourdCanteen.descriptions();
        jdbGourdCanteen.itemTypes(new short[] { 108, 44, 21, 1, 33, 147 });
        jdbGourdCanteen.imageNumber((short) 240);
        jdbGourdCanteen.behaviourType((short) 1);
        jdbGourdCanteen.combatDamage(0);
        jdbGourdCanteen.decayTime(3024000L);
        jdbGourdCanteen.dimensions(10, 10, 20);
        jdbGourdCanteen.primarySkill(-10);
        //jdbGourdCanteen.bodySpaces();
        jdbGourdCanteen.modelName("model.tool.waterskin.");
        jdbGourdCanteen.difficulty(20.0f);
        jdbGourdCanteen.weightGrams(100);
        jdbGourdCanteen.material((byte)14);
        jdbGourdCanteen.value(10000);
        jdbGourdCanteen.isTraded(true);
        //jdbGourdCanteen.armourType();
        try {
            jdbGourdCanteen.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addSeedFlags() {
        Arrays.stream(ItemTemplateFactory.getInstance().getTemplates())
                .filter(itemTemplate -> !itemTemplate.isSeed())
                .filter(itemTemplate -> Arrays.stream(CropsJdb.values())
                        .filter(cropsJdb -> cropsJdb.getProductTemplateId() == itemTemplate.getTemplateId())
                        .count() != 0)
                .forEach(itemTemplate -> itemTemplate.assignTypes(new short[]{ItemTypes.ITEM_TYPE_SEED}));
    }

    private static void cropTypesCodeConvert() throws NotFoundException, CannotCompileException {
        CtClass cropsCt = HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Crops");
        CtField cropTypesCt = cropsCt.getDeclaredField("cropTypes", "[Lcom/wurmonline/server/behaviours/Crops;");
        CtClass addFarmCropsModCt = HookManager.getInstance().getClassPool().get("com.joedobo27.addfarmcrops.AddFarmCropsMod");

        CodeConverter codeConverter = new CodeConverter();
        codeConverter.replaceFieldRead(cropTypesCt, addFarmCropsModCt, "getCropTypes");
        cropsCt.instrument(codeConverter);
    }

    public static Crops[] getCropTypes(Object targetObject){
        return cropTypes;
    }

    private static Crops invokeCropsWrapper(int id, String name, int seed, int product, String measure, double difficulty) {
        try {
            Class cropsClass = Class.forName("com.wurmonline.server.behaviours.Crops");
            Class[] parameterTypes = {Integer.TYPE, String.class, Integer.TYPE, Integer.TYPE, String.class, Double.TYPE};
            Constructor cropsConstructor = cropsClass.getDeclaredConstructor(parameterTypes);
            cropsConstructor.setAccessible(true);
            return (Crops) cropsConstructor.newInstance(id, name, seed, product, measure, difficulty);
        }catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.warning(e.getMessage());
            return null;
        }
    }

    static private void overwriteGetTileType() throws NotFoundException, CannotCompileException{
        CtClass cropsCt = HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Crops");
        CtClass returnType = CtPrimitiveType.byteType;
        CtClass[] paramTypes = {CtPrimitiveType.intType};
        CtMethod getTileTypeCt = cropsCt.getMethod("getTileType", Descriptor.ofMethod(returnType, paramTypes));
        CtClass addFarmCropsModCt = HookManager.getInstance().getClassPool().get("com.joedobo27.addfarmcrops.AddFarmCropsMod");
        CtMethod getTileTypeJdbCt = addFarmCropsModCt.getMethod("getTileType", Descriptor.ofMethod(returnType, paramTypes));
        getTileTypeCt.setBody(getTileTypeJdbCt, null);
    }

    static private void addNewFarmTiles() throws NotFoundException, CannotCompileException, BadBytecode, ClassNotFoundException {
            CtClass ctClass = HookManager.getInstance().getClassPool().get("com.wurmonline.mesh.Tiles$Tile");
            ExtendTileEnum extendTileEnum = new ExtendTileEnum();
            ExtendTileEnum.addExtendEntry("TILE_FIELD3", 44, "Field", "Field", "#473C2F", 0.8f,
                    "img.texture.terrain.farm", new String[]{"ALIGNED"}, 60, (byte) 1, (byte) 1, (byte) 0);
            ExtendTileEnum.addExtendEntry("TILE_FIELD4", 45, "Field", "Field", "#473C2F", 0.8f,
                    "img.texture.terrain.farm", new String[]{"ALIGNED"}, 60, (byte) 1, (byte) 1, (byte) 0);
            ExtendTileEnum.createFieldsInEnum();
            ExtendTileEnum.initiateEnumEntries();
    }

    /**
     * Copy template method for replacing Crops.getTileType() using CtMethod.setbody().
     * Tiles.TILE_TYPE_FIELD = 7; for 0 to 15 cropIds
     * Tiles.TILE_TYPE_FIELD2 = 43; for 16 to 31 cropIds
     * Tiles.TILE_TYPE_FIELD3 = 44; for 32 to 47 cropIds
     * Tiles.TILE_TYPE_FIELD4 = 45; for 48 to 63 cropIds
     * @param cropNumber int primitive, and Id for crops types.
     * @return byte primitive, The tile Id for the appropriate farm tile.
     */
    public static byte getTileType(final int cropNumber) {
        int toReturn = 7;
        if (cropNumber < 16)
            toReturn = 7;
        else if (cropNumber >= 16 && cropNumber < 32)
            toReturn = 43;
        else if (cropNumber >= 32 && cropNumber < 48)
            toReturn = 44;
        else if (cropNumber >= 48 && cropNumber < 64)
            toReturn = 45;
        return (byte) toReturn;
    }

    public static int getLastUsableEntry() {
        return lastUsableEntry;
    }

}
