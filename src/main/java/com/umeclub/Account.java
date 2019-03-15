package com.umeclub;

public class Account {
    private byte[] address;

    /**
     * @return the address
     */
    public byte[] getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(byte[] address) {
        this.address = address;
    }

    public static int allowed(String owner, String spender) {
        System.out.println("allowed " + owner + " to " + spender);
        return 2;
    }

    public static int setBalance(int num) {
        System.out.println("Your Account balance is : " + num);
        return 1;
    }

    public static int setOtherBalance(String address, int num) {
        System.out.println(address + "'s Account balance is : " + num);
        return 1;
    }

}
