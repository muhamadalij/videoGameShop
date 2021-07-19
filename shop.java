import java.util.LinkedList;

import java.lang.String;

import java.io.*;  
import java.util.Scanner;  

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Product {
    Product(String inp) {
        try {
            String[] inputs = inp.split(",");
            id = Integer.parseInt(inputs[0]);
            name = inputs[1];
            price = Double.parseDouble(inputs[2]);
            year = Integer.parseInt(inputs[3]);
            companyName = inputs[4];
            quantity = Integer.parseInt(inputs[5]);
        } catch (Exception error) {
            id = -1;
        }
    }

    Boolean isNull() {
        if (id == -1) return true;
        return false;
    }

    Boolean isEqual(int secondId) {
        if (id == secondId) return true;
        return false;
    }

    Boolean isEnable(int count) {
        if (quantity >= count) return true;
        return false;
    }

    void add(int count) {
        quantity += count;
    }

    void remove(int count) throws Exception {
        if (quantity - count < 0) {
           throw new Exception("kambood mojoodi mahsool "+name);
        }
        quantity-= count;
    }

    double getPrice() {
        return price;
    }

    void print(int count) {
        System.out.println(id + " "  + name + " " + count);
    }

    private int id;
    private String name;
    private double price;
    private int year;
    private String companyName;
    private int quantity;
}

class User {
    void addGame(Product toBuy, int id) {
        for (int i = 0; i < shopingList.size(); i++) {
            if (shopingList.get(i).isEqual(id)) {
                int count = countP.get(i).intValue() + 1;
                countP.remove(i);
                countP.add(i,count);
                return;
            }
        }
        shopingList.add(toBuy);
        countP.add(1);
    }

    void addAmount(double count) {
        amount += count;
    }

    void buy() throws Exception {
        double cost = 0;
        for (int i = 0; i < shopingList.size(); i++) {
            cost += shopingList.get(i).getPrice()*countP.get(i);
            if (!shopingList.get(i).isEnable(countP.get(i))) throw new Exception("kambood mahsool");
        }
        if (cost > amount) throw new Exception("kambood mojudi hesab");
        else {
            for (int i = 0; i < shopingList.size(); i++) shopingList.get(i).remove(countP.get(i));
            amount -= cost;
        }
        shopingList.clear();
        countP.clear(); 
    }

    void showShoppingList() {
        for (int i = 0; i < shopingList.size(); i++) shopingList.get(i).print(countP.get(i));
    }

    void removeGame(int id) throws Exception {
        for (int i = 0; i < shopingList.size(); i++) {
            if (shopingList.get(i).isEqual(id)) {
                shopingList.remove(i);
                countP.remove(i);
                return; 
            }
        }
        throw new Exception("nabood bazi dar sabad");
    }
    
    private LinkedList<Product> shopingList = new LinkedList<Product>();
    private LinkedList<Integer> countP = new LinkedList<Integer>();
    private double amount = 0;
}

class Shop {

    public static void readCSV(String fileName, LinkedList<Product> products)  {   
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Product temp = new Product(data);
                if (!temp.isNull()) products.add(temp);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }  
    }  

    public static Product findProduct(int id, LinkedList<Product> products) throws Exception {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).isEqual(id)) {
                return products.get(i);
            }
        }
        throw new Exception("mahsool yaft nashod");
    }

    public static void run(User user, LinkedList<Product> products) {
        Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            String[] inputs = input.split(" ");
            try {
                if (inputs[0].equals("AddGame") && inputs.length > 1) {
                    Product game = findProduct(Integer.parseInt(inputs[1]), products);
                    user.addGame(game, Integer.parseInt(inputs[1]));
                } else if (inputs[0].equals("RemoveGame") && inputs.length > 1) {
                    user.removeGame(Integer.parseInt(inputs[1]));
                } else if (inputs[0].equals("ShowShopingList")) {
                    user.showShoppingList();
                } else if (inputs[0].equals("AddCredit") && inputs.length > 1) {
                    user.addAmount(Double.parseDouble(inputs[1]));
                } else if (inputs[0].equals("Sumbit")) {
                    user.buy();
                } else {
                    throw new Exception("invalid input");
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
    }

    public static void main(String[] args) {
        User user = new User();
        LinkedList <Product> products = new LinkedList<Product>();
        readCSV("a.csv", products);
        while (true) run(user, products);
    }

}