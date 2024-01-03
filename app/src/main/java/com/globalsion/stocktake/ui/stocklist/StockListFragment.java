package com.globalsion.stocktake.ui.stocklist;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.globalsion.stocktake.R;
import com.globalsion.stocktake.StockItem;
import com.globalsion.stocktake.StockListAdapter;
import com.globalsion.stocktake.databinding.FragmentStocklistBinding;
import com.globalsion.stocktake.tools.DBHelper;
import com.globalsion.stocktake.ui.home.HomeFragment;

import java.util.ArrayList;

public class StockListFragment extends Fragment {

    private StockListViewModel stockListViewModel;
    private FragmentStocklistBinding binding;
    private StockListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        stockListViewModel = new ViewModelProvider(this).get(StockListViewModel.class);
        stockListViewModel.getStockItemListLiveData().observe(getViewLifecycleOwner(), stockItems -> {
            // Update the UI with the new data
            adapter.setStockItems(stockItems);
        });
        binding = FragmentStocklistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button btnHome = binding.btnHome;

        btnHome.setOnClickListener(new BtnHomeClickListener());

        RecyclerView recyclerView = binding.recyclerView;

        // Create a layout manager for the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Get the list of stock items from the database
        DBHelper dbHelper = new DBHelper(getContext());
        ArrayList<StockItem> stockItems = dbHelper.getAllStock();

        // Create an adapter and set it to the RecyclerView
        StockListAdapter adapter = new StockListAdapter(stockItems);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class BtnHomeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Fragment fragment = new HomeFragment();
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
