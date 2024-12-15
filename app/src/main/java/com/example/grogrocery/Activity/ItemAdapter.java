package com.example.grogrocery.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> items;
    private DBHelper dbHelper;

    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        dbHelper = new DBHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_view, parent, false);
        }

        // Get the data item for this position
        Item item = items.get(position);

        // Lookup view for data population
        ImageView itemImage = convertView.findViewById(R.id.item_avatar);
        TextView itemid = convertView.findViewById(R.id.item_ID);
        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemPrice = convertView.findViewById(R.id.item_price);
        TextView itemQty = convertView.findViewById(R.id.item_qty);
        TextView itemCategoryId = convertView.findViewById(R.id.item_category_id);
        TextView itemDescription = convertView.findViewById(R.id.item_description);
        Button btnDeleteItem = convertView.findViewById(R.id.btnDeleteItem);
        Button btnUpdateItem = convertView.findViewById(R.id.btnUpdateItem);

        itemDescription.setText(item.getDescription());
        // Get the data item for this position
        item = items.get(position);


        // Populate the data into the template view using the data object
        if (itemid != null) {
            itemid.setText("ID: " + item.getId());
        }
        if (itemName != null) {
            itemName.setText("Name: " + item.getName());
        }
        if (itemPrice != null) {
            itemPrice.setText("Price: " + item.getPrice());
        }
        if (itemQty != null) {
            itemQty.setText("Quantity: " + item.getQty());
        }
        if (itemCategoryId != null) {
            itemCategoryId.setText("Category ID: " + item.getCategoryId());
        }
        if (itemDescription != null) {
            itemDescription.setText("Description: " + item.getDescription());
        }

        // Convert byte array to bitmap and set image
        byte[] image = item.getAvatar();
        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            itemImage.setImageBitmap(bitmap);
        } else {
            itemImage.setImageResource(R.drawable.hs_red_circle); // or any default image
        }


        // Handle delete button click
        Item Item = item;
        btnDeleteItem.setOnClickListener(v -> {
            // Show an AlertDialog to confirm deletion
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete the item from the database
                        boolean isDeleted = dbHelper.deleteItem(Item.getId());
                        if (isDeleted) {
                            // Remove the item from the list and notify the adapter
                            items.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Dismiss the dialog
                        dialog.dismiss();
                    })
                    .show();
        });


        // Handle update button click
        com.example.grogrocery.Activity.Item finalItem = item;
        btnUpdateItem.setOnClickListener(v -> {
            // Open the update dialog
            openUpdateDialog(finalItem);
        });

        // Return the completed view to render on screen
        return convertView;
    }


    private void openUpdateDialog(Item item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.update_dialog_item, null);
        dialogBuilder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextUpdateName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextUpdatePrice);
        EditText editTextQty = dialogView.findViewById(R.id.editTextUpdateQty);
        EditText editTextCategory = dialogView.findViewById(R.id.editTextUpdateCategory);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextUpdateDescription);  // New description edit text
        Button buttonUpdateRecord = dialogView.findViewById(R.id.buttonUpdateRecord);

        // Populate the dialog with the current item data
        editTextName.setText(item.getName());
        editTextPrice.setText(String.valueOf(item.getPrice()));
        editTextQty.setText(String.valueOf(item.getQty()));
        editTextCategory.setText(item.getCategoryId());
        editTextDescription.setText(item.getDescription());

        // Create and show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // Set the button click listener to update the record
        buttonUpdateRecord.setOnClickListener(v -> {
            String updatedName = editTextName.getText().toString();
            String updatedPrice = editTextPrice.getText().toString();
            String updatedQty = editTextQty.getText().toString();
            String updatedCategoryId = editTextCategory.getText().toString();
            String updatedDescription = editTextDescription.getText().toString();

            if (!updatedName.isEmpty() && !updatedPrice.isEmpty() && !updatedQty.isEmpty() && !updatedCategoryId.isEmpty() && !updatedDescription.isEmpty()) {
                // Call the update method in DBHelper to update the item
                boolean isUpdated = dbHelper.updateItem(
                        String.valueOf(item.getId()),
                        updatedPrice,
                        updatedQty,
                        updatedCategoryId,
                        updatedDescription
                );

                if (isUpdated) {
                    Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Close the dialog
                    // Refresh the list view safely
                    if (items != null) {
                        items.clear();
                        items.addAll(dbHelper.getAllItems());
                        notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, "Failed to update item", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
