package com.joedobo27.addfarmcrops;


import javassist.*;
import javassist.bytecode.*;
import org.gotti.wurmunlimited.modloader.classhooks.CodeReplacer;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.IntStream;

class ExtendTileEnum {
    private static Logger logger = Logger.getLogger(AddFarmCropsMod.class.getName());
    private static ArrayList<ExtendTileEnum.EnumData> toExtendEntries = new ArrayList<>();
    private static final String className = "com.wurmonline.mesh.Tiles$Tile";
    private static int valuesSizerIndex= -1; // the bytecode index which puts a size specifying value on the stack for anewarray.
    private static int indexANEWARRAY = -1;
    private static int populateVALUESIndex = -1; // the bytecode index where references to various enum instances are put in the $VALUES array.
    private static ExtendTileEnum instance;


    ExtendTileEnum() {
        instance = this;
    }

    private void initializeInstance(){
        instance = new ExtendTileEnum();
    }

    public static ExtendTileEnum getInstance() {
        return instance;
    }

    /**
     * A method to create data structures and add record a reference for that object.
     *
     * @param tileId int primitive. A id number for the tile. This is not equal to the enum's default numbering. There are
     *               gaps built into it for adding new tiles later.
     * @param name String type. A name for the tile, ie: Field.
     * @param uniqueName String type. A unique name, ie: Cave exit. It isn't used very often.
     * @param color String type. hex vale in string from.
     * @param speed float primitive. base movement speed on the tile, ex tar is 0.4 and paved tiles are 1.0.
     * @param textureResource String type. An address for where to find the tiles texture graphic.
     * @param flags String type. In WU this is actually a Flag object which is a private inner enum of Tiles. We don't want
     *              to initiate things too early and dealing with private inner classes is a hassle with Java code. Values
     *              passed in as a string array and will be converted at the bytecode level.
     * @param iconId int type. not sure what this does. it doesn't appear to be used anywhere and all the entries use 60.
     * @param waterInfiltration byte primitive. Has something to do with water and at this point it isn't implemented.
     * @param waterReservoir byte primitive. Has something to do with water and at this point it isn't implemented.
     * @param waterLeakage byte primitive. Has something to do with water and at this point it isn't implemented.
     */
    static void addExtendEntry(String fieldName, int tileId, String name, String uniqueName, String color, float speed, String textureResource, String[] flags,
                        int iconId, byte waterInfiltration, byte waterReservoir, byte waterLeakage) {
        if (instance == null)
            throw new EnumExtenderException("Initialize ExtendTileEnum before using addExtendEntry().");
        ExtendTileEnum.EnumData enumData =  instance. new EnumData(fieldName, tileId, name, uniqueName, color, speed, textureResource, flags,
                iconId, waterInfiltration, waterReservoir, waterLeakage);
        toExtendEntries.add(enumData);
    }

    private class EnumData {
        String fieldName;
        int tileId;
        String name;
        String uniqueName;
        String color;
        float speed;
        String textureResource;
        String[] flags;
        int iconId;
        byte waterInfiltration;
        byte waterReservoir;
        byte waterLeakage;

        /**
         * @param fieldName String Object, what will be the enum field name.
         * @param tileId int primitive, a tile id for WU. It does not match up with the enum numbering.
         * @param name String Object, a lower case common name for use in WU.
         * @param uniqueName String Object, a lower case unique name for use in WU. It's not used very often.
         * @param color String Object, a hex value in string from.
         * @param speed float primitive, base movement speed on the tile, ex tar is 0.4 and paved tiles are 1.0.
         * @param textureResource String Object, An address for where to find the tiles texture graphic.
         * @param flags String Object, In WU this is actually a Flag object which is a private inner enum of Tiles. We don't want
         *              to initiate things too early and dealing with private inner classes is a hassle with Java code. Values
         *              passed in as a string array and will be converted at the bytecode level.
         * @param iconId int primitive, no idea what this is. Its not used and all entries have the same number, 60.
         * @param waterInfiltration byte primitive, related to adding dynamic water to WU, not implemented.
         * @param waterReservoir byte primitive, related to adding dynamic water to WU, not implemented.
         * @param waterLeakage byte primitive, related to adding dynamic water to WU, not implemented.
         */
        EnumData(String fieldName, int tileId, String name, String uniqueName, String color, float speed, String textureResource, String[] flags,
                 int iconId, byte waterInfiltration, byte waterReservoir, byte waterLeakage){
            this.fieldName = fieldName;
            this.tileId = tileId;
            this.name = name;
            this.uniqueName = uniqueName;
            this.color = color;
            this.speed = speed;
            this.textureResource = textureResource;
            this.flags = flags;
            this.iconId = iconId;
            this.waterInfiltration = waterInfiltration;
            this.waterReservoir = waterReservoir;
            this.waterLeakage = waterLeakage;
        }
    }

    /**
     * Intended to be used in WurmServerMod-initiate section and it's for bytecode changes. This adds field objects to the enum class.
     *
     * @throws CannotCompileException forwarded, Javassist stuff.
     */
    static void createFieldsInEnum() throws CannotCompileException, NotFoundException {
        if (toExtendEntries.size() == 0){
            throw new EnumExtenderException("Can not create fields without values in toExtendEntries arrayList.");
        }

        CtClass enumCtClass = HookManager.getInstance().getClassPool().get(className);
        for (ExtendTileEnum.EnumData enumData : toExtendEntries) {
            CtField field = new CtField(enumCtClass, enumData.fieldName, enumCtClass);
            field.setModifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL | Modifier.ENUM);
            enumCtClass.addField(field);
        }
    }

    /**
     * Goes through the enum class's initiator to find index positions.
     *
     * @throws BadBytecode forwarded, Javassist stuff.
     */
    private static void initiatorParser() throws BadBytecode, NotFoundException {
        CtClass tileCt = HookManager.getInstance().getClassPool().get(className);
        CodeIterator codeIterator = tileCt.getClassInitializer().getMethodInfo().getCodeAttribute().iterator();
        // Get the byte code instruction index for
        // 1) size value for ANEWARRAY,
        // 2) the VALUES array assignment or population.

        BytecodeTools b = new BytecodeTools(tileCt.getClassFile().getConstPool());
        int constPoolValuesIndex = b.findFieldIndex(Opcode.PUTSTATIC, "$VALUES", "[Lcom/wurmonline/mesh/Tiles$Tile;",
                "com.wurmonline.mesh.Tiles$Tile");
        codeIterator.begin();
        int lastIndex = 0;
        while (codeIterator.hasNext()){
            int instructionIndex = codeIterator.next();
            int opCode = codeIterator.byteAt(instructionIndex);
            switch (opCode){
                case Opcode.ANEWARRAY :
                    valuesSizerIndex = lastIndex;
                    indexANEWARRAY = instructionIndex;
                    break;
                case Opcode.PUTSTATIC :
                    int cpAddress = codeIterator.u16bitAt(instructionIndex+1);
                    if (cpAddress == constPoolValuesIndex){
                        populateVALUESIndex = instructionIndex;
                    }
                    break;
                default:
                    break;
            }
            lastIndex = instructionIndex;
        }
        if (valuesSizerIndex == -1 || indexANEWARRAY == -1 || populateVALUESIndex == -1)
            throw new EnumExtenderException(String.format("bytecode indexing failed: valuesSizerIndex= %d, indexANEWARRAY= %d, populateVALUESIndex= %d",
                    valuesSizerIndex, indexANEWARRAY, populateVALUESIndex));
    }

    /**
     * This method uses JA bytecode to inject into the Enum's class initiator in order to expand the enum's $VALUES field.
     *
     * @param expansion int value, expand the $VALUES field's size this much.
     * @throws BadBytecode forwarded, Javassist stuff.
     */
    private static void resizeEnumVALUES(int expansion) throws BadBytecode, ClassNotFoundException, NotFoundException {
        CtClass tileCt = HookManager.getInstance().getClassPool().get(className);
        CodeIterator codeIterator = tileCt.getClassInitializer().getMethodInfo().getCodeAttribute().iterator();

        BytecodeTools find = new BytecodeTools(tileCt.getClassFile().getConstPool());
        find.addInteger(find.getInteger(codeIterator, valuesSizerIndex));
        find.addOpcode(Opcode.ANEWARRAY);
        find.findClassIndex(className);

        BytecodeTools replace = new BytecodeTools(tileCt.getClassFile().getConstPool());
        replace.addInteger(replace.getInteger(codeIterator, valuesSizerIndex) + expansion);
        replace.addOpcode(Opcode.ANEWARRAY);
        replace.findClassIndex(className);

        CodeReplacer codeReplacer = new CodeReplacer(tileCt.getClassInitializer().getMethodInfo().getCodeAttribute());
        codeReplacer.replaceCode(find.get(), replace.get());
    }

    /**
     * This method builds bytecode to inject into the enum's initiator. The injected code initializes new enum entries and adds
     * a reference of that new object to the $VALUES array.
     *
     * @throws BadBytecode forwarded, JA stuff.
     * @throws ClassNotFoundException forwarded, JA stuff.
     * @throws NotFoundException forwarded, JA stuff.
     */
    static void initiateEnumEntries() throws BadBytecode, ClassNotFoundException, NotFoundException {
        CtClass tileCt = HookManager.getInstance().getClassPool().get(className);
        CodeIterator initiatorCodeIterator = tileCt.getClassInitializer().getMethodInfo().getCodeAttribute().iterator();

        initiatorParser();
        BytecodeTools enumInitiator = new BytecodeTools(tileCt.getClassFile().getConstPool());
        BytecodeTools populateVALUES = new BytecodeTools(tileCt.getClassFile().getConstPool());
        int extensionCounter = 0;
        int valuesSize = enumInitiator.getInteger(initiatorCodeIterator, valuesSizerIndex);
        // Construct the two bytecode objects to be inserted. The multiple enumData in toExtendEntries are combined into one
        // long bytecode sequence and inserted at the proper point.
        for (ExtendTileEnum.EnumData enumData : toExtendEntries) {
            enumInitiator.addOpcode(Opcode.NEW);
            enumInitiator.findClassIndex("com.wurmonline.mesh.Tiles$Tile");
            enumInitiator.addOpcode(Opcode.DUP);
            enumInitiator.addLdc(enumData.fieldName);
            enumInitiator.addInteger(valuesSize + extensionCounter);
            enumInitiator.addInteger(enumData.tileId);
            enumInitiator.addLdc(enumData.name);
            enumInitiator.addLdc(enumData.uniqueName);
            enumInitiator.addLdc(enumData.color);
            enumInitiator.addFloat(enumData.speed);
            enumInitiator.addLdc(enumData.textureResource);
            enumInitiator.addInteger(enumData.flags.length);
            enumInitiator.addOpcode(Opcode.ANEWARRAY);
            enumInitiator.findClassIndex("com.wurmonline.mesh.Tiles$Flag");
            int ordinal = 0;
            for (String string : enumData.flags){
                enumInitiator.addOpcode(Opcode.DUP);
                enumInitiator.addInteger(ordinal++);
                enumInitiator.findFieldIndex(Opcode.GETSTATIC, string,
                        "Lcom/wurmonline/mesh/Tiles$Flag;", "com.wurmonline.mesh.Tiles$Flag");
                enumInitiator.addOpcode(Opcode.AASTORE);
            }
            enumInitiator.addInteger(enumData.iconId);
            enumInitiator.addInteger(enumData.waterInfiltration);
            enumInitiator.addInteger(enumData.waterReservoir);
            enumInitiator.addInteger(enumData.waterLeakage);
            enumInitiator.findMethodIndex(Opcode.INVOKESPECIAL, "<init>",
                    "(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;FLjava/lang/String;[Lcom/wurmonline/mesh/Tiles$Flag;IBBB)V",
                    "com.wurmonline.mesh.Tiles$Tile");
            enumInitiator.addFieldIndex(Opcode.PUTSTATIC, enumData.fieldName, "Lcom/wurmonline/mesh/Tiles$Tile;",
                    "com.wurmonline.mesh.Tiles$Tile");

            populateVALUES.addOpcode(Opcode.DUP);
            populateVALUES.addInteger(valuesSize + extensionCounter);
            extensionCounter++;
            populateVALUES.findFieldIndex(Opcode.GETSTATIC, enumData.fieldName, "Lcom/wurmonline/mesh/Tiles$Tile;",
                    "com.wurmonline.mesh.Tiles$Tile");
            populateVALUES.addOpcode(Opcode.AASTORE);
        }
        // Do bytecode changes from the bottom up so bytecode indexes don't change after every insert.
        initiatorCodeIterator.insert(populateVALUESIndex, populateVALUES.get());
        resizeEnumVALUES(toExtendEntries.size());
        initiatorCodeIterator.insert(valuesSizerIndex, enumInitiator.get());
        tileCt.getClassInitializer().getMethodInfo().rebuildStackMapIf6(HookManager.getInstance().getClassPool(),
                tileCt.getClassFile());
    }
}
