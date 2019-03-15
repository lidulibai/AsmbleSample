package com.umeclub;

public class RC20Coin {
    private String name;
    private int num;
    private String symbol;
    private int precision;

    RC20Coin(String name, int num, String symbol, int precision) {
        this.name = name;
        this.num = num;
        this.symbol = symbol;
        this.precision = precision;
        System.out.println(this);
    }
    /**
     * @return the precision
     */
    public int getPrecision() {
        return precision;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }
    /**
     * @param num the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }
    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }
    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    /**
     * @param precision the precision to set
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public String toString() {
        return "{\n\tTokenName: " + getName() + ",\n\tTokenAmount: " + getNum() + ",\n\tTokenSymbol: " + getSymbol() + ",\n\tTokenPrecision: " + getPrecision() + "\n}";
    }
}
