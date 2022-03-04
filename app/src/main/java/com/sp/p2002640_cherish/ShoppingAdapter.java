package com.sp.p2002640_cherish;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.Holder>{
    private Context context;
    private ArrayList<ShoppingModel> arraylist_shopping;
    // database object
    ShoppingHelper databaseHelper_shopping;

    public ShoppingAdapter(Context context, ArrayList<ShoppingModel> arraylist_shopping) {
        this.context = context;
        this.arraylist_shopping = arraylist_shopping;
        databaseHelper_shopping = new ShoppingHelper(context);
    }


    @NonNull
    @Override
    public ShoppingAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.shopping_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingAdapter.Holder holder_shopping, int position_shopping) {

        ShoppingModel model = arraylist_shopping.get(position_shopping);
        // get views
        String id = model.getId();
        String name_shopping = model.getName_shopping();
        byte[] bitMapBytes_shopping = model.getImage();
        String quantity = model.getQuantity();
        String quantity_details = "Quantity:" + " " + quantity;
        String type = model.getType();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitMapBytes_shopping , 0, bitMapBytes_shopping .length);
        String addTimeStamp_shopping = model.getAddTimeStamp_shopping();
        String updateTimeStamp_shopping= model.getUpdateTimeStamp_shopping();

        holder_shopping.shoppingfoodimage.setImageBitmap(bitmap);
        holder_shopping.name_shopping.setText(name_shopping);
        holder_shopping.quantity.setText(quantity_details);
        holder_shopping.type.setText(type);
        holder_shopping.ib_edit_shopping.setOnClickListener(v -> {
            editDialog(
                    ""+position_shopping,
                    id,
                    name_shopping,
                    quantity,
                    type,
                    updateTimeStamp_shopping,
                    addTimeStamp_shopping,
                    bitMapBytes_shopping);
        });
    }
    private void editDialog(String position, String id_shopping, String name_shopping, String quantity,String type , String bitMapBytes, String addTimeStamp_shopping, byte[] updateTimeStamp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        deleteDialog(id_shopping);
    }

    private void deleteDialog(final String id_shopping) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper_shopping.deleteInfo(id_shopping);
                ((ShoppingInfoDisplay)context).onResume();
                Toast.makeText(context,"Record Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();

    }

    @Override
    public int getItemCount() {
        return (arraylist_shopping == null) ? 0 : arraylist_shopping.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView shoppingfoodimage;
        TextView name_shopping, quantity, type;
        ImageButton ib_edit_shopping;

        public Holder(@NonNull View itemView) {
            super(itemView);

            shoppingfoodimage = (ImageView) itemView.findViewById(R.id.shoppingfoodimage);
            name_shopping = (TextView) itemView.findViewById(R.id.rv_foodname_shopping);
            quantity = (TextView) itemView.findViewById(R.id.rv_quantity);
            type = (TextView) itemView.findViewById(R.id.rv_type);
            ib_edit_shopping = (ImageButton) itemView.findViewById(R.id.ib_edit_shopping);
        }
    }
}
