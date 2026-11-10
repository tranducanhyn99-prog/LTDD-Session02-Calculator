package vn.edu.course.calculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import vn.edu.course.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CalculatorViewModel viewModel;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

        setSupportActionBar(binding.toolbar);

        NavHostFragment navHost = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (navHost != null) {
            navController = navHost.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.calculatorFragment, R.id.historyFragment).build();
            NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.bottomNav, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return (navController != null && NavigationUI.navigateUp(navController, appBarConfiguration))
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            int count = viewModel.getCurrentState().getHistory().size();
            startActivity(new Intent(this, AboutActivity.class)
                    .putExtra(AboutActivity.EXTRA_COUNT, count));
            return true;
        } else if (id == R.id.action_help) {
            openHelp();
            return true;
        }
        return (navController != null && NavigationUI.onNavDestinationSelected(item, navController))
                || super.onOptionsItemSelected(item);
    }

    private void openHelp() {
        Uri page = Uri.parse(getString(R.string.help_url));
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, page));
        } catch (ActivityNotFoundException e) {
            Snackbar.make(binding.getRoot(), R.string.no_browser, Snackbar.LENGTH_SHORT).show();
        }
    }
}
