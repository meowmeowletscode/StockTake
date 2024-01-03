package com.globalsion.stocktake.ui.stock;

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
import com.globalsion.stocktake.databinding.FragmentStockBinding;
import com.globalsion.stocktake.tools.DBHelper;
import com.globalsion.stocktake.ui.home.HomeFragment;

public class StockFragment extends Fragment {

    private StockViewModel stockViewModel;
    private FragmentStockBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stockViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        binding = FragmentStockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText txtStock = binding.txtStock;
        final EditText txtLocation = binding.txtLocation;
        final EditText txtQuantity = binding.txtQuantity;

        final Button btnClear = binding.btnClear;
        final Button btnSave = binding.btnSave;
        final Button btnHome = binding.btnHome;

        btnClear.setOnClickListener(new BtnClearClickListener(txtStock, txtLocation, txtQuantity));
        btnSave.setOnClickListener(new BtnSaveClickListener(txtStock, txtLocation, txtQuantity));
        btnHome.setOnClickListener(new BtnHomeClickListener());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class BtnClearClickListener implements View.OnClickListener{

        private EditText txtStock;
        private EditText txtLocation;
        private EditText txtQuantity;

        public BtnClearClickListener(EditText txtStock, EditText txtLocation, EditText txtQuantity) {
            this.txtStock = txtStock;
            this.txtLocation = txtLocation;
            this.txtQuantity = txtQuantity;
        }

        @Override
        public void onClick(View v){
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

    private class BtnSaveClickListener implements View.OnClickListener{

        private EditText txtStock;
        private EditText txtLocation;
        private EditText txtQuantity;

        public BtnSaveClickListener(EditText txtStock, EditText txtLocation, EditText txtQuantity) {
            this.txtStock = txtStock;
            this.txtLocation = txtLocation;
            this.txtQuantity = txtQuantity;
        }

        @Override
        public void onClick(View v) {
            String stockId = txtStock.getText().toString();
            String location = txtLocation.getText().toString();
            String quantityString = txtQuantity.getText().toString();

            if (stockId.isEmpty() || location.isEmpty() || quantityString.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityString);

            DBHelper dbHelper = new DBHelper(getActivity());

            boolean success = dbHelper.insertStock(stockId, location, quantity);

            if (success) {
                Toast.makeText(getActivity(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                new BtnClearClickListener(txtStock, txtLocation, txtQuantity).onClick(null);
            } else {
                Toast.makeText(getActivity(), "Failed to save data. The format is incorrect or stock id already exists", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class BtnHomeClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v){
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
}