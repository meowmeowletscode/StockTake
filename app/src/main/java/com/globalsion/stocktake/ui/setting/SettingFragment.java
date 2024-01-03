package com.globalsion.stocktake.ui.setting;

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
import com.globalsion.stocktake.databinding.FragmentSettingBinding;
import com.globalsion.stocktake.tools.DBHelper;
import com.globalsion.stocktake.ui.home.HomeFragment;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText txtUserID = binding.txtUserID;
        final EditText txtPassword = binding.txtPassword;

        final Button btnAddUser = binding.btnAddUser;
        final Button btnEditUser = binding.btnEditUser;
        final Button btnDeleteUser = binding.btnDeleteUser;
        final Button btnHome = binding.btnHome;
        final Button btnClear = binding.btnClear;

        btnAddUser.setOnClickListener(new BtnAddUserClickListener(txtUserID, txtPassword));
        btnEditUser.setOnClickListener(new BtnEditUserClickListener(txtUserID, txtPassword));
        btnDeleteUser.setOnClickListener(new BtnDeleteUserClickListener(txtUserID, txtPassword));
        btnHome.setOnClickListener(new BtnHomeClickListener());
        btnClear.setOnClickListener(new BtnClearClickListener(txtUserID, txtPassword));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class BtnAddUserClickListener implements View.OnClickListener{

        private EditText txtUserID;
        private EditText txtPassword;

        public BtnAddUserClickListener(EditText txtUserID, EditText txtPassword) {
            this.txtUserID = txtUserID;
            this.txtPassword = txtPassword;
        }

        @Override
        public void onClick(View v) {
            String userId = txtUserID.getText().toString();
            String password = txtPassword.getText().toString();

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(getActivity());

            boolean success = dbHelper.addUser(userId, password);

            if (success) {
                Toast.makeText(getActivity(), "User added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to save data. The format is incorrect or User ID already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BtnEditUserClickListener implements View.OnClickListener {

        private EditText txtUserID;
        private EditText txtPassword;

        public BtnEditUserClickListener(EditText txtUserID, EditText txtPassword) {
            this.txtUserID = txtUserID;
            this.txtPassword = txtPassword;
        }

        @Override
        public void onClick(View v) {
            String userId = txtUserID.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(getActivity());

            try {
                Cursor cursor = dbHelper.getUserData(userId);
                if (cursor != null) {
                    boolean hasData = cursor.moveToFirst();

                    if (hasData) {
                        boolean success = dbHelper.updateUser(userId, password);

                        if (success) {
                            Toast.makeText(getActivity(), "User data updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to update user data.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "User ID does not exist. Please check and try again", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BtnDeleteUserClickListener implements View.OnClickListener {

        private EditText txtUserID;
        private EditText txtPassword;

        public BtnDeleteUserClickListener(EditText txtUserID, EditText txtPassword) {
            this.txtUserID = txtUserID;
            this.txtPassword = txtPassword;
        }

        @Override
        public void onClick(View v) {
            String userId = txtUserID.getText().toString().trim();

            if (userId.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in a valid Stock ID", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(getActivity());

            try {
                Cursor cursor = dbHelper.getUserData(userId);
                if (cursor != null) {
                    boolean hasData = cursor.moveToFirst();

                    if (hasData) {
                        boolean success = dbHelper.deleteUser(userId);

                        if (success) {
                            Toast.makeText(getActivity(), "User data deleted successfully", Toast.LENGTH_SHORT).show();
                            new BtnClearClickListener(txtUserID, txtPassword).onClick(null);
                        } else {
                            Toast.makeText(getActivity(), "Failed to delete user data.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "User ID does not exist. Please check and try again", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
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

    private class BtnClearClickListener implements View.OnClickListener {

        private EditText txtUserID;
        private EditText txtPassword;

        public BtnClearClickListener(EditText txtUserID, EditText txtPassword) {
            this.txtUserID = txtUserID;
            this.txtPassword = txtPassword;
        }

        @Override
        public void onClick(View v) {
            if (txtUserID != null) {
                txtUserID.setText("");
            }
            if (txtPassword != null) {
                txtPassword.setText("");
            }
        }
    }

}