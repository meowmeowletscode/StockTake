package com.globalsion.stocktake.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.globalsion.stocktake.R;
import com.globalsion.stocktake.databinding.FragmentHomeBinding;
import com.globalsion.stocktake.tools.ExportFunc;
import com.globalsion.stocktake.ui.stockedit.StockEditFragment;
import com.globalsion.stocktake.ui.stock.StockFragment;
import com.globalsion.stocktake.ui.stocklist.StockListFragment;


import java.io.IOException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageButton btnScan = binding.btnScan;
        final ImageButton btnStockEdit = binding.btnStockEdit;
        final ImageButton btnStockList = binding.btnStockList;
        final ImageButton btnExport = binding.btnExport;

        btnScan.setOnClickListener(new BtnScanClickListener());
        btnStockEdit.setOnClickListener(new BtnStockEditClickListener());
        btnStockList.setOnClickListener(new BtnStockListClickListener());
        btnExport.setOnClickListener(new BtnExportClickListener());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class BtnScanClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Fragment fragment = new StockFragment();
            replaceFragment(fragment, getString(R.string.menu_stocksave));
        }
    }

    public class BtnStockEditClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Fragment fragment = new StockEditFragment();
            replaceFragment(fragment, getString(R.string.menu_stockedit));
        }
    }

    public class BtnStockListClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Fragment fragment = new StockListFragment();
            replaceFragment(fragment, getString(R.string.menu_stocklist));
        }
    }

    public class BtnExportClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ExportFunc exCSV = new ExportFunc();
            try {
                exCSV.exportCSV(getContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void replaceFragment(Fragment someFragment, String title) {
        requireActivity().setTitle(title);
        Log.d("TitleChange", "Title changed to: " + title);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}