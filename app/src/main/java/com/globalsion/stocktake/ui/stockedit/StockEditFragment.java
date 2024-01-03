package com.globalsion.stocktake.ui.stockedit;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.globalsion.stocktake.R;
import com.globalsion.stocktake.databinding.FragmentStockeditBinding;
import com.globalsion.stocktake.tools.DBHelper;
import com.globalsion.stocktake.ui.home.HomeFragment;

public class StockEditFragment extends Fragment {

    private StockEditViewModel stockEditViewModel;
    private FragmentStockeditBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stockEditViewModel = new ViewModelProvider(this).get(StockEditViewModel.class);

        binding = FragmentStockeditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText txtStock = binding.txtStock;
        final EditText txtLocation = binding.txtLocation;
        final EditText txtQuantity = binding.txtQuantity;

        final Button btnClear = binding.btnClear;
        final Button btnSearch = binding.btnSearch;
        final Button btnEdit = binding.btnEdit;
        final Button btnHome = binding.btnHome;
        final Button btnDelete = binding.btnDelete;

        btnClear.setOnClickListener(new BtnClearClickListener(txtStock, txtLocation, txtQuantity));
        btnSearch.setOnClickListener(new BtnSearchClickListener(txtStock, txtLocation, txtQuantity));
        btnEdit.setOnClickListener(new BtnEditClickListener(txtStock, txtLocation, txtQuantity));
        btnHome.setOnClickListener(new BtnHomeClickListener());
        btnDelete.setOnClickListener(new BtnDeleteClickListener(txtStock, txtLocation, txtQuantity));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class BtnClearClickListener implements View.OnClickListener {

        private EditText txtStock;
        private EditText txtLocation;
        private EditText txtQuantity;

        public BtnClearClickListener(EditText txtStock, EditText txtLocation, EditText txtQuantity) {
            this.txtStock = txtStock;
            this.txtLocation = txtLocation;
            this.txtQuantity = txtQuantity;
        }

        @Override
        public void onClick(View v) {
            if (txtStock != null) {
                txtStock.setText("");
            }
            if (txtLocation != null) {
                txtLocation.setText("");
            }
            if (txtQuantity != null) {
                txtQuantity.setText("");
            }
        }
    }

    private class BtnSearchClickListener implements View.OnClickListener {

        private EditText txtStock;
        private EditText txtLocation;
        private EditText txtQuantity;

        public BtnSearchClickListener(EditText txtStock, EditText txtLocation, EditText txtQuantity) {
            this.txtStock = txtStock;
            this.txtLocation = txtLocation;
            this.txtQuantity = txtQuantity;
        }

        @Override
        public void onClick(View v) {
            String stockId = txtStock.getText().toString();

            if (stockId.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in a valid Stock ID", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(getActivity());

            Cursor cursor = dbHelper.getStockData(stockId);

            if (cursor != null) {
                boolean hasData = cursor.moveToFirst();

                if (hasData) {
                    String location = cursor.getString(cursor.getColumnIndex("location"));
                    int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                    txtLocation.setText(location);
                    txtQuantity.setText(String.valueOf(quantity));

                    Toast.makeText(getActivity(), "Stock data found.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Stock ID does not exist. Please check and try again", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        }
    }

    private class BtnEditClickListener implements View.OnClickListener {

        private EditText txtStock;
        private EditText txtLocation;
        private EditText txtQuantity;

        public BtnEditClickListener(EditText txtStock, EditText txtLocation, EditText txtQuantity) {
            this.txtStock = txtStock;
            this.txtLocation = txtLocation;
            this.txtQuantity = txtQuantity;
        }

        @Override
        public void onClick(View v) {
            String stockId = txtStock.getText().toString().trim();
            String location = txtLocation.getText().toString().trim();
            String quantityStr = txtQuantity.getText().toString().trim();

            if (stockId.isEmpty() || location.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer quantity;

            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the case where quantity is not a valid integer
                Toast.makeText(getActivity(), "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(getActivity());

            try {
                Cursor cursor = dbHelper.getStockData(stockId);

                if (cursor != null) {
                    boolean hasData = cursor.moveToFirst();

                    if (hasData) {
                        boolean success = dbHelper.updateStock(stockId, location, quantity);

                        if (success) {
                            Toast.makeText(getActivity(), "Stock data updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to update stock data.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Stock ID does not exist. Please check and try again", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BtnHomeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Fragment fragment = null;
            fragment = new HomeFragment();
            replaceFragment(fragment);
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class BtnDeleteClickListener implements View.OnClickListener {

        private EditText txtStock;
        private EditText txtLocation;
        private EditText txtQuantity;

        public BtnDeleteClickListener(EditText txtStock, EditText txtLocation, EditText txtQuantity) {
            this.txtStock = txtStock;
            this.txtLocation = txtLocation;
            this.txtQuantity = txtQuantity;
        }

        @Override
        public void onClick(View v) {
            String stockId = txtStock.getText().toString().trim();

            if (stockId.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in a valid Stock ID", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(getActivity());

            try {
                Cursor cursor = dbHelper.getStockData(stockId);

                if (cursor != null) {
                    boolean hasData = cursor.moveToFirst();

                    if (hasData) {
                        boolean success = dbHelper.deleteStock(stockId);

                        if (success) {
                            Toast.makeText(getActivity(), "Stock data deleted successfully", Toast.LENGTH_SHORT).show();
                            new BtnClearClickListener(txtStock, txtLocation, txtQuantity).onClick(null);
                        } else {
                            Toast.makeText(getActivity(), "Failed to delete stock data.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Stock ID does not exist. Please check and try again", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}