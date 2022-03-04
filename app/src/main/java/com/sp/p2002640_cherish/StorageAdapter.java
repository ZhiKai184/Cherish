package com.sp.p2002640_cherish;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.Holder>{
    private Context context;
    private ArrayList<StorageModel> arraylist;
    // databse object
    StorageHelper databaseHelper;

    public StorageAdapter(Context context, ArrayList<StorageModel> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        databaseHelper = new StorageHelper(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.storage_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        StorageModel model = arraylist.get(position);
        // get views
        String id = model.getId();
        String name = model.getName();
        byte[] bitMapBytes = model.getImage();
        String date = model.getDate();
        String price = model.getPrice();
        String weight = model.getWeight();
        String price_and_weight = "" + weight + " SGD, " + price + " kg";
        String expiry_date = "" + date + " (expiry date)";
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitMapBytes , 0, bitMapBytes .length);
        String addTimeStamp = model.getAddTimeStamp();
        String updateTimeStamp = model.getUpdateTimeStamp();

        holder.storagefoodimage.setImageBitmap(bitmap);
        holder.name.setText(name);
        holder.expiry_date.setText(expiry_date);
        holder.price_and_weight.setText(price_and_weight);

      holder.ib_edit.setOnClickListener(v -> {
            editDialog(
                    ""+position,
                    id,
                    name,
                    date,
                    price,
                    weight,
                    addTimeStamp,
                    updateTimeStamp,
                    bitMapBytes);
        });
    }

   private void editDialog(String position, String id, String name, String date, String price, String weight, String bitMapBytes, String addTimeStamp, byte[] updateTimeStamp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
                deleteDialog(id);
            }

    private void deleteDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteInfo(id);
                ((StorageInfoDisplay)context).onResume();
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
        return arraylist.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        ImageView storagefoodimage;
        TextView name, expiry_date, price_and_weight;
        ImageButton ib_edit;

        public Holder(@NonNull View itemView) {
            super(itemView);

            storagefoodimage = (ImageView) itemView.findViewById(R.id.storagefoodimage);
            name = (TextView) itemView.findViewById(R.id.rv_foodname);
            expiry_date = (TextView) itemView.findViewById(R.id.rv_expirydate);
            price_and_weight = (TextView) itemView.findViewById(R.id.rv_foodpriceandweight);
            ib_edit = (ImageButton) itemView.findViewById(R.id.ib_edit);
        }
    }
}

