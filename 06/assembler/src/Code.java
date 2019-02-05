import java.util.HashMap;

/**
 * Code class of The Element of Computer Architecture.
 * Convert mnemonic to binary code.
 *
 * @author ayakatakuriko
 * @data 2019/02/05
 */

class Code {
    private HashMap<String, String> destMap;
    private HashMap<String, String> compMap;
    private HashMap<String, String> jumpMap;

    public Code() {
        this.destMap = new HashMap<>();
        this.compMap = new HashMap<>();
        this.jumpMap = new HashMap<>();

        // Initialize destMap
        destMap.put(null, "000");

        destMap.put("M", "001");

        destMap.put("D", "010");

        destMap.put("MD", "011");
        destMap.put("DM", "011");

        destMap.put("A", "100");

        destMap.put("AM", "101");
        destMap.put("MA", "101");

        destMap.put("AD", "110");
        destMap.put("DA", "110");

        destMap.put("AMD", "111");
        destMap.put("ADM", "111");
        destMap.put("MAD", "111");
        destMap.put("MDA", "111");
        destMap.put("DAM", "111");
        destMap.put("DMA", "111");

        // Initialize jumpMap
        jumpMap.put(null, "000");
        jumpMap.put("JGT", "001");
        jumpMap.put("JEQ", "010");
        jumpMap.put("JGE", "011");
        jumpMap.put("JLT", "100");
        jumpMap.put("JNE", "101");
        jumpMap.put("JLE", "110");
        jumpMap.put("JMP", "111");

        // Initialize compMap
        compMap.put("0", "0101010");
        compMap.put("1", "0111111");
        compMap.put("-1", "0111010");
        compMap.put("D", "0001100");
        compMap.put("A", "0110000");
        compMap.put("!D", "0001101");
        compMap.put("!A", "0110001");
        compMap.put("-D", "0001111");
        compMap.put("-A", "0110011");
        compMap.put("D+1", "0011111");
        compMap.put("A+1", "0110111");
        compMap.put("D-1", "0001110");
        compMap.put("A-1", "0110010");
        compMap.put("D+A", "0000010");
        compMap.put("D-A", "0010011");
        compMap.put("A-D", "0000111");
        compMap.put("D&A", "0000000");
        compMap.put("D|A", "0010101");
        compMap.put("M", "1110000");
        compMap.put("!M", "1110001");
        compMap.put("-M", "1110011");
        compMap.put("M+1", "1110111");
        compMap.put("M-1", "1110010");
        compMap.put("D+M", "1000010");
        compMap.put("D-M", "1010011");
        compMap.put("M-D", "1000111");
        compMap.put("D&M", "1000000");
        compMap.put("D|M", "1010101");
    }

    public String dest(String mnemonic) {
        return destMap.get(mnemonic);
    }

    public String comp(String mnemonic) {
        return compMap.get(mnemonic);
    }

    public String jump(String mnemonic) {
        return compMap.get(mnemonic);
    }
}
