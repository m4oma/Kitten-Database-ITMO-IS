package utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.function.IntPredicate;


public class Scanner {
    private final Reader reader;
    private final char[] buffer;
    private int bufferSize = 0;
    private int bufferPointer = 0;
    private String token = null;
    private int skippedLineCount = 0;
    private IntPredicate isSep;
    private IntPredicate isNotSep;

    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static boolean defaultDetector(int chr) {
        return chr == '\n' || chr == '\r' || Character.getType(chr) == Character.SPACE_SEPARATOR;
    }


    public Scanner(Reader reader) {
        this(reader, DEFAULT_BUFFER_SIZE);
    }

    public Scanner(InputStream in) {
        this(new BufferedReader(new InputStreamReader(in)), DEFAULT_BUFFER_SIZE);
    }

    public Scanner(Reader reader, int bufferSize) {
        if (reader == null) {
            throw new NullPointerException();
        }

        this.reader = reader;
        this.buffer = new char[bufferSize];
        setSeparatorDetector(Scanner::defaultDetector);
    }

    private boolean checkBuffer() throws IOException {
        if (bufferPointer < bufferSize) {
            return true;
        }

        bufferPointer = 0;
        bufferSize = Math.max(reader.read(buffer), 0);
        return bufferSize != 0;
    }

    private int readChar() throws IOException {
        return readIf(null);
    }

    private int readIf(IntPredicate cond) throws IOException {
        if (checkBuffer()) {
            int res = buffer[bufferPointer];
            if (cond == null || cond.test(res)) {
                bufferPointer++;
                return res;
            }

            return -1;
        }

        return -1;
    }

    private boolean parseNextToken() throws IOException {
        //read all the separators
        skippedLineCount = 0;
        for (int chr = readIf(isSep), prev = '\0'; chr != -1;
             prev = chr, chr = readIf(isSep)) {
            if (chr == '\n' && prev != '\r' || chr == '\r') {
                skippedLineCount++;
            }
        }

        //read the token until separator found
        StringBuilder sb = new StringBuilder();
        for (int chr = readIf(isNotSep); chr != -1; chr = readIf(isNotSep)) {
            sb.append((char)chr);
        }

        //return
        token = !sb.isEmpty() ? sb.toString() : null;
        return token != null;
    }

    public String nextString() throws NoSuchElementException, IOException {
        if (token == null && !parseNextToken()) {
            throw new NoSuchElementException();
        }

        String result = token;
        token = null;
        return result;
    }

    public boolean hasNext() throws IOException {
        return token != null || parseNextToken();
    }

    private String format10Radix(String value) {
        StringBuilder converted = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char chr = value.charAt(i);
            converted.append(Character.isLetter(chr) ? (char)((chr - 'a') % 10 + '0') : chr);
        }
        return converted.toString();
    }

    public int nextInt() throws NumberFormatException, NoSuchElementException, IOException {
        String result = nextString().toLowerCase();
        if (result.charAt(result.length() - 1) == 'o') {
            return Integer.parseUnsignedInt(result.substring(0,result.length()-1), 8);
        }
        return Integer.parseInt(format10Radix(result));
    }

    public int linesSkipped() {
        return skippedLineCount;
    }

    public void close() throws IOException {
        reader.close();
    }

    public void setSeparatorDetector(IntPredicate value) {
        if (value == null) {
            throw new NullPointerException();
        }

        this.isSep = v -> (v == '\n' || v == '\r' || value.test(v));
        this.isNotSep = v -> !isSep.test(v);
    }
}
