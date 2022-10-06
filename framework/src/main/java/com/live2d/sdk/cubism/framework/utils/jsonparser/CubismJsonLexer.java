/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */


package com.live2d.sdk.cubism.framework.utils.jsonparser;

import com.live2d.sdk.cubism.framework.exception.CubismException;
import com.live2d.sdk.cubism.framework.exception.CubismJsonParseException;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

/**
 * This class offers a function of JSON lexer.
 */
class CubismJsonLexer {
    /**
     * Package-private constructor
     *
     * @param json string of JSON
     */
    public CubismJsonLexer(String json) {
        assert json != null;

        StringReader reader = new StringReader(json);
        this.json = new LineNumberReader(reader);
    }

    /**
     * Get a next token.
     */
    public CubismJsonToken getNextToken() throws CubismJsonParseException, IOException {
        // Skip blank characters
        while (isWhiteSpaceChar(nextChar)) {
            updateNextChar();
        }

        // A Number token
        // A process when beginning at minus sign
        if (nextChar == '-') {
            StringBuilder value = new StringBuilder("-");
            updateNextChar();

            if (Character.isDigit(nextChar)) {
                value.append(buildNumber());
                return new CubismJsonToken(Double.parseDouble(value.toString()));
            } else {
                throw new CubismJsonParseException("Number's format is incorrect.", json.getLineNumber());
            }
        }
        // A process when beginning at a number except 0.
        else if (Character.isDigit(nextChar)) {
            return new CubismJsonToken(Double.parseDouble(buildNumber().toString()));
        }
        // true
        else if (nextChar == 't') {
            StringBuilder value = new StringBuilder();
            value.append(nextChar);
            updateNextChar();

            for (int i = 0; i < 3; i++) {
                value.append(nextChar);
                updateNextChar();
            }

            // If "value" does not create true value, send an exception.
            if (!value.toString().equals("true")) {
                throw new CubismJsonParseException("Boolean's format or spell is incorrect.", json.getLineNumber());
            }

            return new CubismJsonToken(true);
        }
        // false
        else if (nextChar == 'f') {
            StringBuilder value = new StringBuilder();
            value.append(nextChar);
            updateNextChar();

            for (int i = 0; i < 4; i++) {
                value.append(nextChar);
                updateNextChar();
            }

            // If the value does not equals to "false" value, send the exception.
            if (!value.toString().equals("false")) {
                throw new CubismJsonParseException("Boolean's format or spell is incorrect.", json.getLineNumber());
            }

            return new CubismJsonToken(false);
        }
        // null
        else if (nextChar == 'n') {
            StringBuilder value = new StringBuilder();
            value.append(nextChar);
            updateNextChar();

            for (int i = 0; i < 3; i++) {
                value.append(nextChar);
                updateNextChar();
            }

            // If the JSON value does not equal to the "null" value, send an exception.
            if (!value.toString().equals("null")) {
                throw new CubismJsonParseException("Boolean's format or spell is incorrect.", json.getLineNumber());
            }

            return new CubismJsonToken();
        } else if (nextChar == '{') {
            updateNextChar();
            return new CubismJsonToken(CubismJsonToken.TokenType.LBRACE);
        } else if (nextChar == '}') {
            updateNextChar();
            return new CubismJsonToken(CubismJsonToken.TokenType.RBRACE);
        } else if (nextChar == '[') {
            updateNextChar();
            return new CubismJsonToken(CubismJsonToken.TokenType.LSQUARE_BRACKET);
        } else if (nextChar == ']') {
            updateNextChar();
            return new CubismJsonToken(CubismJsonToken.TokenType.RSQUARE_BRACKET);
        }
        // If next character is double quote, string token is created.
        else if (nextChar == '"') {
            StringBuilder value = new StringBuilder();
            updateNextChar();

            // Until closing by double quote("), it is continued to read.
            while (nextChar != '"') {
                // Consider a escape sequence.
                if (nextChar == '\\') {
                    updateNextChar();
                    value.append(buildEscapedString());
                } else {
                    value.append(nextChar);
                }
                updateNextChar();
            }
            updateNextChar();
            return new CubismJsonToken(value.toString());
        }
        // Colon(:)
        else if (nextChar == ':') {
            updateNextChar();
            return new CubismJsonToken(CubismJsonToken.TokenType.COLON);
        }
        // Comma(,)
        else if (nextChar == ',') {
            updateNextChar();
            return new CubismJsonToken(CubismJsonToken.TokenType.COMMA);
        }

        throw new CubismJsonParseException("The JSON is not closed properly, or there is some other malformed form.", json.getLineNumber());
    }

    /**
     * Return current line number.
     *
     * @return current line number
     */
    public int getCurrentLineNumber() {
        return json.getLineNumber();
    }

    /**
     * Build number string.
     *
     * @return a number string
     *
     * @throws CubismJsonParseException the exception at failing to parse
     */
    private StringBuilder buildNumber() throws CubismJsonParseException, IOException {
        StringBuilder value = new StringBuilder();

        if (nextChar == '0') {
            value.append(nextChar);
            updateNextChar();

            value.append(buildDoubleOrExpNumber());

        } else {
            value.append(nextChar);
            updateNextChar();

            // Repeat processes until appearing a character except dot, exponential expression or number.
            while (Character.isDigit(nextChar)) {
                value.append(nextChar);
                updateNextChar();
            }
            value.append(buildDoubleOrExpNumber());
        }
        return value;
    }

    /**
     * Build double or exponential number.
     *
     * @return double or exponential number
     *
     * @throws CubismJsonParseException the exception at failing to parse
     */
    private StringBuilder buildDoubleOrExpNumber() throws CubismJsonParseException, IOException {
        StringBuilder value = new StringBuilder();

        // If the next character is dot, floating point number is created.
        if (nextChar == '.') {
            value.append(buildDoubleNumber());
        }
        // If there is an e or E, it is considered an exponential expression.
        if (nextChar == 'e' || nextChar == 'E') {
            value.append(buildExponents());
        }
        return value;
    }

    /**
     * Return floating point number as strings(StringBuilder).
     *
     * @return the parsed floating point number
     *
     * @throws CubismJsonParseException the exception at failing to parse
     */
    private StringBuilder buildDoubleNumber() throws CubismJsonParseException, IOException {
        StringBuilder value = new StringBuilder(".");
        updateNextChar();

        // If the character following dot sign is not a number, an exception is thrown.
        if (!Character.isDigit(nextChar)) {
            throw new CubismJsonParseException("Number's format is incorrect.", json.getLineNumber());
        }
        do {
            value.append(nextChar);
            updateNextChar();
        } while (Character.isDigit(nextChar));

        return value;
    }

    /**
     * Build a number string used an exponential expression.
     *
     * @return the parsed number string used an exponential expression
     *
     * @throws CubismJsonParseException the exception at failing to parse
     */
    private StringBuilder buildExponents() throws CubismJsonParseException, IOException {
        StringBuilder value = new StringBuilder(String.valueOf(nextChar));
        updateNextChar();

        // Handle cases where a number is preceded by a sign.
        if (nextChar == '+') {
            value.append(nextChar);
            updateNextChar();
        } else if (nextChar == '-') {
            value.append(nextChar);
            updateNextChar();
        }
        // If the character is not a number or a sign, an exception is thrown.
        if (!Character.isDigit(nextChar)) {
            throw new CubismJsonParseException(value + "\n: " + "Exponent value's format is incorrect.", json.getLineNumber());
        }

        do {
            value.append(nextChar);
            updateNextChar();
        } while (Character.isDigit(nextChar));

        return value;
    }

    /**
     * Build a string used an escape sequence.
     *
     * @return Escaped string
     *
     * @throws CubismJsonParseException the exception at failing to parse
     */
    private StringBuilder buildEscapedString() throws CubismJsonParseException, IOException {
        StringBuilder value = new StringBuilder();

        switch (nextChar) {
            case '"':
            case '\\':
            case '/':
                value.append(nextChar);
                break;
            case 'b':
                value.append("\b");
                break;
            case 'f':
                value.append("\f");
                break;
            case 'n':
                value.append("\n");
                break;
            case 'r':
                value.append("\r");
                break;
            case 't':
                value.append("\t");
                break;
            case 'u': {
                StringBuilder tmp = new StringBuilder();
                tmp.append('\\');
                tmp.append('u');
                for (int i = 0; i < 4; i++) {
                    updateNextChar();
                    tmp.append(nextChar);
                }
                // Check whether it is hex number. If there is a problem, an exception is thrown.
                String tmp2 = tmp.toString();
                if (!tmp2.matches("\\\\u[a-fA-F0-9]{4}")) {
                    throw new CubismJsonParseException(tmp + "\n: " + "The unicode notation is incorrect.", json.getLineNumber());
                }

                value.append(tmp2);
                break;
            }
        }
        return value;
    }

    /**
     * Whether a character is white space character.
     *
     * @param c checked character
     * @return If the character is white space character, return true
     */
    private boolean isWhiteSpaceChar(char c) {
        return (c == ' ' || c == '\r' || c == '\n' || c == '\t');
    }

    /**
     * Read a next character
     */
    private void updateNextChar() throws IOException {
        // Read the next line when the character count reaches the end of the line.
        if (lineIndex == lineString.length() - 1) {
            String newLine;
//            try {
//                newLine = json.readLine();
//            } catch (IOException e) {
//                throw new CubismJsonParseException("It seems that an error has occured in the input/output processing", e);
//            }
            newLine = json.readLine();


            if (newLine == null) {
                lineString = null;
                nextChar = '\0';
                return;
            }

            lineString = newLine + " ";
            lineIndex = 0;
            nextChar = lineString.charAt(lineIndex);
        } else {
            lineIndex++;
            nextChar = lineString.charAt(lineIndex);
        }
    }


    /**
     * JSON string
     */
//    private final String _rowJsonString;
    private final LineNumberReader json;
    /**
     * the length of the JSON string
     */
//    private final int _jsonLength;

    private int lineIndex = -1;
    private String lineString = "";

    /**
     * the next character
     */
    private char nextChar = ' ';
    /**
     * Current index of character in JSON string
     */
    private int charIndex;
}
