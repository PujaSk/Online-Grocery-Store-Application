package com.example.grogrocery.SqliteDatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.grogrocery.Activity.Bill;
import com.example.grogrocery.Activity.Category;
import com.example.grogrocery.Activity.Item;
import com.example.grogrocery.Activity.ItemReport;
import com.example.grogrocery.Activity.OrderReport;
import com.example.grogrocery.Activity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String dbname = "Grocery.db";

    public DBHelper(Context context) {
        super(context, dbname, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        String qry = ("create Table Reg_Master(cid integer primary key autoincrement, name text, email text UNIQUE, password text, confirmpass text, phno number, city text)");
        db.execSQL(qry);

        String qry1 = ("create Table Login_Master(email text primary key, password text)");
        db.execSQL(qry1);
        db.execSQL("insert into Login_Master values('admin@gmail.com','admin')");

        String qry2 = ("create Table IF NOT EXISTS Category_Master(id text primary key not null , name text not null, avatar blob not null)");
         db.execSQL(qry2);

        String qry3 = ("create Table Item_Master(iid integer primary key autoincrement, name text, price integer, qty integer, avatar blob, ctg_id text, description text, FOREIGN KEY(ctg_id) REFERENCES Category_Master(id))");
        db.execSQL(qry3);

        String createCartMaster = "CREATE TABLE Cart_Master(cart_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, item_id INTEGER, quantity INTEGER, FOREIGN KEY(user_id) REFERENCES Reg_Master(cid), FOREIGN KEY(item_id) REFERENCES Item_Master(iid))";
        db.execSQL(createCartMaster);

        String createBillMaster = "CREATE TABLE Bill_Master (" +
                "bill_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "total_amount REAL, " +
                "bill_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES Reg_Master(cid));";
        db.execSQL(createBillMaster);



        String createOrderMaster = "CREATE TABLE Order_Master(order_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, item_id INTEGER, quantity INTEGER, bill_id INTEGER, order_date DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(user_id) REFERENCES Reg_Master(cid), FOREIGN KEY(item_id) REFERENCES Item_Master(iid), FOREIGN KEY(bill_id) REFERENCES Bill_Master(bill_id))";
        db.execSQL(createOrderMaster);




        //Log.d("DB","Table created successfully");
        //Log.d("DB","Record inserted successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Reg_Master");
        db.execSQL("DROP TABLE IF EXISTS Login_Master");
        db.execSQL("DROP TABLE IF EXISTS Category_Master");
        db.execSQL("DROP TABLE IF EXISTS Item_Master");
        db.execSQL("DROP TABLE IF EXISTS Cart_Master");
        db.execSQL("DROP TABLE IF EXISTS Order_Master");
        db.execSQL("DROP TABLE IF EXISTS Bill_Master");
        onCreate(db);


        // Add bill_id column to Order_Master
        String alterOrderMaster = "ALTER TABLE Order_Master ADD COLUMN bill_id INTEGER REFERENCES Bill_Master(bill_id)";
        db.execSQL(alterOrderMaster);


    }


// Add Record in Reg_Master -->  User Login Details..
    public boolean addRecord(String p1, String p2, String p3, String p4, String p5, String p6)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put("name",p1);
        cv.put("email",p2);
        cv.put("password",p3);
        cv.put("confirmpass",p4);
        cv.put("phno",p5);
        cv.put("city",p6);

        long res = db.insert("Reg_Master",null,cv);

        if(res == -1)
            return false;
        else
            return true;
    }
    public boolean checkUseremail(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Reg_Master where email = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }
    public boolean checkUser(String em, String pwd){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Reg_Master where email = ? and password = ?", new String[]{em,pwd});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

// Check the Admin Id and Password..
    public boolean checkAdmin(String em, String pwd){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Login_Master where email = ? and password = ?", new String[]{em,pwd});
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }



// Add Items in Category_Master Database..
    public boolean insertCat(String p1, String p2, byte[] p3)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put("id",p1);
        cv.put("name",p2);
        cv.put("avatar",p3);

        long res = db.insert("Category_Master",null,cv);

        if(res == -1)
            return false;
        else
            return true;
    }
//Check the Category Id ..
    public boolean checkCatId(String catid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Category_Master where id = ?", new String[]{catid});
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
// Displaying all the category items from the Category_Master table
    public ArrayList<HashMap<String, Object>> getAllCategories() {
        ArrayList<HashMap<String, Object>> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, name, avatar FROM Category_Master", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> category = new HashMap<>();
                category.put("id", cursor.getString(0));
                category.put("name", cursor.getString(1));
                category.put("avatar", cursor.getBlob(2));  // Fetch image as a blob
                categoryList.add(category);

                // Add this log statement to debug
                Log.d("Category", "ID: " + cursor.getString(0) + ", Name: " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    // Updating only id and name of Category_Master table
    public boolean updateCategory(String id, String name, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("avatar", image); // Ensure the avatar column is updated with the new image bytes

        // Updating the category
        int rowsAffected = db.update("Category_Master", values, "id = ?", new String[]{id});
        return rowsAffected > 0;
    }


// Add Items in Item_Master Database..
    public boolean insertItem(String p1, String p2, String p3, byte[] p4, String p5, String p6)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put("name",p1);
        cv.put("price",p2);
        cv.put("qty",p3);
        cv.put("avatar",p4);
        cv.put("ctg_id",p5);
        cv.put("Description", p6);

        long res = db.insert("Item_Master",null,cv);

        if(res == -1)
            return false;
        else
            return true;
    }
// Displaying all the items from the Item_Master table
@SuppressLint("Range")
public ArrayList<Item> getAllItems() {
    ArrayList<Item> itemList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    String query = "SELECT * FROM Item_Master";
    Cursor cursor = db.rawQuery(query, null);

    if (cursor.moveToFirst()) {
        do {
            // Create an Item object and add it to the list
            int id = cursor.getInt(cursor.getColumnIndex("iid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int price = cursor.getInt(cursor.getColumnIndex("price"));
            int qty = cursor.getInt(cursor.getColumnIndex("qty"));
            String ctgId = cursor.getString(cursor.getColumnIndex("ctg_id"));
            byte[] avatar = cursor.getBlob(cursor.getColumnIndex("avatar"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            Item item = new Item(id, name, price, qty, avatar, ctgId, description);
            itemList.add(item);
        } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return itemList;
}
// Updating only price, qty and catID and description of Item_Master table
    public boolean updateItem(String id, String price, String qty, String categoryId, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("price", price);
        contentValues.put("qty", qty);
        contentValues.put("ctg_id", categoryId);
        contentValues.put("Description", description);

        // Log the values being updated
        Log.d("DBHelper", "Updating item with ID: " + id +
                ", Price: " + price +
                ", Quantity: " + qty +
                ", CategoryId: " + categoryId +
                ", Description: " + description);
        // Perform the update
        // Updating row where ID matches
        int result = db.update("Item_Master", contentValues, "iid = ?", new String[]{id});
        // Log the result of the update
        Log.d("DBHelper", "Update result: " + result);
        // Check if any rows were updated
        if (result > 0) {
            return true;  // Update was successful
        } else {
            return false;  // No rows were updated
        }
    }

 // Fetch all users from the Reg_Master table
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the Reg_Master table
        Cursor cursor = db.rawQuery("SELECT cid,name, email, phno, city FROM Reg_Master", null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> user = new HashMap<>();
                user.put("cid",cursor.getString(0));
                user.put("name", cursor.getString(1));
                user.put("email", cursor.getString(2));
                user.put("phno", cursor.getString(3));
                user.put("city", cursor.getString(4));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }

// if category exist then only it will add item in Item_Master TAble
    public boolean doesCategoryExist(String categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM Category_Master WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryId});

        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;  // If count is greater than 0, the category exists
        }

        cursor.close();
        db.close();
        return exists;
    }

// to delete the item from Item_Master table
    public boolean deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("Item_Master", "iid = ?", new String[]{String.valueOf(itemId)});
        return rowsAffected > 0;
    }

// Displaying items for a specific category in HomeScreen
    @SuppressLint("Range")
    public ArrayList<HashMap<String, Object>> getItemsByCategoryId(String categoryId) {
        ArrayList<HashMap<String, Object>> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT iid, name, price, qty, avatar, ctg_id, description FROM Item_Master WHERE ctg_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryId});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> item = new HashMap<>();
                item.put("iid", cursor.getInt(cursor.getColumnIndex("iid")));
                item.put("name", cursor.getString(cursor.getColumnIndex("name")));
                item.put("price", cursor.getInt(cursor.getColumnIndex("price")));
                item.put("qty", cursor.getInt(cursor.getColumnIndex("qty")));
                item.put("avatar", cursor.getBlob(cursor.getColumnIndex("avatar")));
                item.put("ctg_id", cursor.getString(cursor.getColumnIndex("ctg_id")));
                item.put("description", cursor.getString(cursor.getColumnIndex("description")));
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }

// Add to Cart
    public boolean addToCart(int userId, int itemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_id", userId);
        cv.put("item_id", itemId);
        cv.put("quantity", quantity);

        // Check if the item is already in the cart
        Cursor cursor = db.rawQuery("SELECT quantity FROM Cart_Master WHERE user_id=? AND item_id=?", new String[]{String.valueOf(userId), String.valueOf(itemId)});
        if (cursor.moveToFirst()) {
            // Update the existing quantity
            int existingQty = cursor.getInt(0);
            ContentValues updateCv = new ContentValues();
            updateCv.put("quantity", existingQty + quantity);
            int result = db.update("Cart_Master", updateCv, "user_id=? AND item_id=?", new String[]{String.valueOf(userId), String.valueOf(itemId)});
            cursor.close();
            return result > 0;
        } else {
            // Insert new item into cart
            long result = db.insert("Cart_Master", null, cv);
            cursor.close();
            return result != -1;
        }
    }

// Update Cart Quantity
    public boolean updateCartQuantity(int userId, int itemId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (newQuantity <= 0) {
            // Remove the item from cart
            int result = db.delete("Cart_Master", "user_id=? AND item_id=?", new String[]{String.valueOf(userId), String.valueOf(itemId)});
            return result > 0;
        } else {
            ContentValues cv = new ContentValues();
            cv.put("quantity", newQuantity);
            int result = db.update("Cart_Master", cv, "user_id=? AND item_id=?", new String[]{String.valueOf(userId), String.valueOf(itemId)});
            return result > 0;
        }
    }

// Get Cart Items for a User
    public ArrayList<HashMap<String, Object>> getCartItems(int userId) {
        ArrayList<HashMap<String, Object>> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Item_Master.iid, Item_Master.name, Item_Master.price, Item_Master.qty, Item_Master.avatar, Cart_Master.quantity FROM Cart_Master JOIN Item_Master ON Cart_Master.item_id = Item_Master.iid WHERE Cart_Master.user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> cartItem = new HashMap<>();
                cartItem.put("iid", cursor.getInt(0));
                cartItem.put("name", cursor.getString(1));
                cartItem.put("price", cursor.getInt(2));
                cartItem.put("stockQty", cursor.getInt(3)); // Available stock
                cartItem.put("avatar", cursor.getBlob(4));
                cartItem.put("quantity", cursor.getInt(5)); // Quantity in cart
                cartList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }
/*
// Checkout Cart
    public boolean checkoutCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Get all cart items for the user
            ArrayList<HashMap<String, Object>> cartItems = getCartItems(userId);
            for (HashMap<String, Object> cartItem : cartItems) {
                int itemId = (int) cartItem.get("iid");
                int cartQty = (int) cartItem.get("quantity");
                int stockQty = (int) cartItem.get("stockQty");

                if (stockQty < cartQty) {
                    // Not enough stock, abort transaction
                    Log.e("DBHelper", "Insufficient stock for itemId: " + itemId);
                    return false;
                }

                // Insert into Order_Master
                ContentValues orderCv = new ContentValues();
                orderCv.put("user_id", userId);
                orderCv.put("item_id", itemId);
                orderCv.put("quantity", cartQty);
                long insertResult = db.insert("Order_Master", null, orderCv);
                if (insertResult == -1) {
                    // Insertion failed, abort transaction
                    return false;
                }

                // Update Item_Master quantity
                ContentValues itemCv = new ContentValues();
                itemCv.put("qty", stockQty - cartQty);
                int updateResult = db.update("Item_Master", itemCv, "iid=?", new String[]{String.valueOf(itemId)});
                if (updateResult == 0) {
                    // Update failed, abort transaction
                    return false;
                }
            }

            // Clear the cart
            db.delete("Cart_Master", "user_id=?", new String[]{String.valueOf(userId)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("DBHelper", "Error during checkout", e);
            return false;
        } finally {
            db.endTransaction();
        }
    }

 */

// Get Orders for a User
    @SuppressLint("Range")
    public ArrayList<HashMap<String, Object>> getUserOrders(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, Object>> orders = new ArrayList<>();

        // SQL query to fetch orders along with bill_id
        String query = "SELECT O.order_id, O.order_date, I.name AS item_name, O.quantity, I.price, " +
                "O.quantity * I.price AS total_price, O.bill_id " +
                "FROM Order_Master O " +
                "JOIN Item_Master I ON O.item_id = I.iid " +
                "WHERE O.user_id = ? " +
                "ORDER BY O.order_date DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> order = new HashMap<>();
                order.put("order_id", cursor.getInt(cursor.getColumnIndex("order_id")));
                order.put("order_date", cursor.getString(cursor.getColumnIndex("order_date")));
                order.put("item_name", cursor.getString(cursor.getColumnIndex("item_name")));
                order.put("quantity", cursor.getInt(cursor.getColumnIndex("quantity")));
                order.put("price", cursor.getDouble(cursor.getColumnIndex("price"))); // Ensure Double
                order.put("total_price", cursor.getDouble(cursor.getColumnIndex("total_price"))); // Ensure Double
                order.put("bill_id", cursor.getLong(cursor.getColumnIndex("bill_id"))); // Ensure Long
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }


    // Get user_id by email
    @SuppressLint("Range")
    public int getUserIdByEmail(String email) {
        Log.d("DBHelper", "Fetching user ID for email: " + email);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cid FROM Reg_Master WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex("cid"));
            Log.d("DBHelper", "Found user ID: " + userId + " for email: " + email);
            cursor.close();
            return userId;
        }
        Log.d("DBHelper", "No user found with email: " + email);
        if (cursor != null) {
            cursor.close();
        }
        return -1; // Return -1 if no user is found
    }

// Search items by name
    public ArrayList<Item> searchItems(String query) {
        ArrayList<Item> searchResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Use LIKE for partial matching and handle case insensitivity
        String sqlQuery = "SELECT * FROM Item_Master WHERE name LIKE ?";
        Cursor cursor = db.rawQuery(sqlQuery, new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("iid"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty"));
                String ctgId = cursor.getString(cursor.getColumnIndexOrThrow("ctg_id"));
                byte[] avatar = cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                Item item = new Item(id, name, price, qty, avatar, ctgId, description);
                searchResults.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return searchResults;
    }

    // Method to get user details by userId
    public User getUserDetails(int userId) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT name, email, phno, city FROM Reg_Master WHERE cid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phno"));
            String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
            user = new User(userId, name, email, phone, city);
        }
        cursor.close();
        db.close();
        return user;
    }

    // Method to update user's name and city
    public boolean updateUserNameAndCity(int userId, String newName, String newCity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", newName);
        cv.put("city", newCity);

        int result = db.update("Reg_Master", cv, "cid = ?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }









    public long checkoutCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long billId = -1; // Initialize billId

        try {
            // Calculate total amount based on the formula
            double totalAmount = calculateTotalAmount(userId);

            // Insert into Bill_Master first
            ContentValues billCv = new ContentValues();
            billCv.put("user_id", userId);
            billCv.put("total_amount", totalAmount);
            billId = db.insert("Bill_Master", null, billCv); // Insert and get billId

            if (billId == -1) {
                Log.e("DBHelper", "Failed to insert into Bill_Master");
                return -1; // Indicate failure
            }

            // Get all cart items for the user
            ArrayList<HashMap<String, Object>> cartItems = getCartItems(userId);
            for (HashMap<String, Object> cartItem : cartItems) {
                int itemId = (int) cartItem.get("iid");
                int cartQty = (int) cartItem.get("quantity");
                int stockQty = (int) cartItem.get("stockQty");

                if (stockQty < cartQty) {
                    Log.e("DBHelper", "Insufficient stock for itemId: " + itemId);
                    return -1; // Indicate failure
                }

                // Insert into Order_Master
                ContentValues orderCv = new ContentValues();
                orderCv.put("user_id", userId);
                orderCv.put("item_id", itemId);
                orderCv.put("quantity", cartQty);
                orderCv.put("bill_id", billId); // Link the order to the bill
                long insertResult = db.insert("Order_Master", null, orderCv);
                if (insertResult == -1) {
                    Log.e("DBHelper", "Failed to insert into Order_Master for itemId: " + itemId);
                    return -1; // Indicate failure
                }

                // Update Item_Master quantity
                ContentValues itemCv = new ContentValues();
                itemCv.put("qty", stockQty - cartQty);
                int updateResult = db.update("Item_Master", itemCv, "iid=?", new String[]{String.valueOf(itemId)});
                if (updateResult == 0) {
                    Log.e("DBHelper", "Failed to update Item_Master for itemId: " + itemId);
                    return -1; // Indicate failure
                }
            }

            // Clear the cart
            db.delete("Cart_Master", "user_id=?", new String[]{String.valueOf(userId)});
            db.setTransactionSuccessful();
            return billId; // Return the new billId
        } catch (Exception e) {
            Log.e("DBHelper", "Error during checkout", e);
            return -1; // Indicate failure
        } finally {
            db.endTransaction();
        }
    }


    @SuppressLint("Range")
    public double calculateTotalAmount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalAmount = 0;
        double tax =0;
        double subtotal=0;

        // Query to fetch the price and quantity of items in the cart
        String query = "SELECT c.quantity, i.price " +
                "FROM Cart_Master c " +
                "JOIN Item_Master i ON c.item_id = i.iid " +
                "WHERE c.user_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                // Apply the formula: ((price * quantity) * 0.1) + 50 + (price * quantity)
                tax = (price * quantity) * 0.1;
                subtotal =  (price * quantity);
                double itemTotal = (tax + subtotal) ;
                totalAmount += itemTotal;

            } while (cursor.moveToNext());
        }
        cursor.close();
        totalAmount += 50;
        return totalAmount;
    }


    @SuppressLint("Range")
    public ArrayList<HashMap<String, Object>> getOrdersByBillId(long billId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, Object>> orders = new ArrayList<>();

        String query = "SELECT O.quantity, I.name AS item_name, I.price " +
                "FROM Order_Master O " +
                "JOIN Item_Master I ON O.item_id = I.iid " +
                "WHERE O.bill_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(billId)});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> order = new HashMap<>();
                order.put("quantity", cursor.getInt(cursor.getColumnIndex("quantity")));
                order.put("item_name", cursor.getString(cursor.getColumnIndex("item_name")));
                order.put("price", cursor.getDouble(cursor.getColumnIndex("price")));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }


    @SuppressLint("Range")
    public HashMap<String, Object> getBillDetails(long billId) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Object> billDetails = null;

        String query = "SELECT bill_date, total_amount FROM Bill_Master WHERE bill_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(billId)});

        if (cursor.moveToFirst()) {
            billDetails = new HashMap<>();
            billDetails.put("bill_date", cursor.getString(cursor.getColumnIndex("bill_date")));
            billDetails.put("total_amount", cursor.getDouble(cursor.getColumnIndex("total_amount")));
        }
        cursor.close();
        return billDetails;
    }

    // Fetch Orders for a User (Updated with price_per_product and total_amt)
    @SuppressLint("Range")
    public List<Bill> fetchBills() {
        List<Bill> billList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Bill_Master", null);

        // Log column names for debugging
        Log.d("DBHelper", "Columns in Bill_Master: " + Arrays.toString(cursor.getColumnNames()));

        // Retrieve column indices
        int billIdIndex = cursor.getColumnIndex("bill_id");
        int userIdIndex = cursor.getColumnIndex("user_id");
        int totalAmountIndex = cursor.getColumnIndex("total_amount");
        int billDateIndex = cursor.getColumnIndex("bill_date");

        // Check if columns exist
        if (billIdIndex == -1 || userIdIndex == -1 || totalAmountIndex == -1 || billDateIndex == -1) {
            Log.e("DBHelper", "Required columns not found in Bill_Master table.");
            cursor.close();
            return billList; // Return empty list or handle as needed
        }

        if (cursor.moveToFirst()) {
            do {
                int billId = cursor.getInt(billIdIndex);
                int userId = cursor.getInt(userIdIndex);
                double totalAmount = cursor.getDouble(totalAmountIndex);
                String billDate = cursor.getString(billDateIndex);

                Bill bill = new Bill(billId, userId, totalAmount, billDate);
                billList.add(bill);
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHelper", "No records found in Bill_Master table.");
        }

        cursor.close();
        return billList;
    }


    @SuppressLint("Range")
    public List<ItemReport> fetchItemReports() {
        List<ItemReport> itemReportList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT iid, name, price, qty FROM Item_Master", null);

        if (cursor.moveToFirst()) {
            do {
                int itemId = cursor.getInt(cursor.getColumnIndex("iid"));
                String itemName = cursor.getString(cursor.getColumnIndex("name"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                int quantity = cursor.getInt(cursor.getColumnIndex("qty"));

                ItemReport itemReport = new ItemReport(itemId, itemName, price, quantity);
                itemReportList.add(itemReport);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemReportList;
    }


    // Method to fetch Order Reports
    @SuppressLint("Range")
    public List<OrderReport> fetchOrderReports() {
        List<OrderReport> orderReportList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT O.order_id, R.name AS user_name, I.name AS item_name, O.quantity, B.total_amount, O.order_date " +
                "FROM Order_Master O " +
                "JOIN Reg_Master R ON O.user_id = R.cid " +
                "JOIN Item_Master I ON O.item_id = I.iid " +
                "JOIN Bill_Master B ON O.bill_id = B.bill_id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndex("order_id"));
                String userName = cursor.getString(cursor.getColumnIndex("user_name"));
                String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("total_amount"));
                String orderDate = cursor.getString(cursor.getColumnIndex("order_date"));

                OrderReport orderReport = new OrderReport(orderId, userName, itemName, quantity, totalAmount, orderDate);
                orderReportList.add(orderReport);
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHelper", "No records found in Order_Master table.");
        }

        cursor.close();
        return orderReportList;
    }






}

