package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import javax.swing.tree.TreeNode;

public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings = new String[128];

    private String s1 = "";

    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /*
     private double Occurence(String s, char letter){
        double count = 0;
        char[] charArray = s.toCharArray();
        for(int i = 0; i<charArray.length; i++){
            if(charArray[i] == letter){
                count++;
            }
        }
        return count;
    } */

    public void makeSortedList() {
        StdIn.setFile(fileName);

        sortedCharFreqList = new ArrayList<CharFreq>();
        int[] charArray = new int[128];
        double total = 0;

        while(StdIn.hasNextChar()){
            char v = StdIn.readChar();
            int charNum = (int)v;
            charArray[charNum]++;
            total++;
        }

        for(int i = 0; i < charArray.length; i++){
            if(charArray[i] != 0){
                CharFreq node = new CharFreq((char)i, (charArray[i] / total));
                sortedCharFreqList.add(node);
                if((charArray[i] / total) == 1){
                    if(i == 128){
                        CharFreq node1 = new CharFreq((char)0, 0);
                        sortedCharFreqList.add(node1);
                    } else {
                        CharFreq node1 = new CharFreq((char)(i+1), 0);
                        sortedCharFreqList.add(node1);
                    }
                }
            }
        }    
        Collections.sort(sortedCharFreqList);
    }

    public void makeTree() {
        Queue<TreeNode> source = new Queue<>();
        Queue<TreeNode> target = new Queue<>();

        for(int i = 0; i < sortedCharFreqList.size(); i++){
            CharFreq x = sortedCharFreqList.get(i);
            TreeNode y = new TreeNode(x, null, null);
            source.enqueue(y);
        }

        while(!source.isEmpty() || target.size() != 1){
            TreeNode d1 = null;
            TreeNode d2 = null;
            if(target.isEmpty()){
                d1 = source.dequeue();
                d2 = source.dequeue();
            } else if(source.isEmpty()) {
                d1 = target.dequeue();
                d2 = target.dequeue();
            } else if(source.peek().getData().getProbOcc()==target.peek().getData().getProbOcc() || source.peek().getData().compareTo(target.peek().getData()) <= 0){
                d1 = source.dequeue();
                if(source.isEmpty()){
                    d2 = target.dequeue();
                } else if(source.peek().getData().getProbOcc()==target.peek().getData().getProbOcc() || source.peek().getData().compareTo(target.peek().getData()) <= 0){
                    d2 = source.dequeue();
                } else {
                    d2 = target.dequeue();
                }
            } else {
                 d1 = target.dequeue();
                if(target.isEmpty()){
                    d2 = source.dequeue();
                } else if(source.peek().getData().getProbOcc()==target.peek().getData().getProbOcc() || source.peek().getData().compareTo(target.peek().getData()) <= 0){
                    d2 = source.dequeue();
                } else {
                    d2 = target.dequeue();
                }
            }
            CharFreq w = new CharFreq(null, (d1.getData().getProbOcc() + d2.getData().getProbOcc()));
            TreeNode q = new TreeNode(w, d1, d2);
            target.enqueue(q); 
        }
        huffmanRoot = target.dequeue();
    }

    public void makeEncodings() {
        encodings = recurse(encodings, s1, huffmanRoot);
    }

    public static String[] recurse(String[] array, String s, TreeNode r){
        if(r.getLeft() != null){
            recurse(array, s + "0", r.getLeft());
        } 
        if(r.getRight() != null){
            recurse(array, s + "1", r.getRight());
        }
        if(r.getRight() == null && r.getLeft() == null){
            array[(int)r.getData().getCharacter()] = s;
        }
        return array;
    } 

    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String bit = "";
        while(StdIn.hasNextChar()){
            bit += encodings[(int)StdIn.readChar()];
        }
        writeBitString(encodedFile, bit);
    }
    
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

        String x = readBitString(encodedFile);
        TreeNode ptr = huffmanRoot;
        for(int i = 0; i < x.length(); i++){
            if(x.charAt(i) == '0' && ptr.getLeft() != null){
                ptr = ptr.getLeft();
            } else if(x.charAt(i) == '1' && ptr.getRight() != null){
                ptr = ptr.getRight();
            }
            if(ptr.getData().getCharacter() != null){
                StdOut.print(ptr.getData().getCharacter());
                ptr = huffmanRoot;
            }
        }
    }

    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
