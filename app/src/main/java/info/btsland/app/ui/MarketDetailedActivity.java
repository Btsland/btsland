package info.btsland.app.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.btsland.app.R;
import info.btsland.app.model.Market;

public class MarketDetailedActivity extends AppCompatActivity {
    private Market market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detailed);
        market= (Market) getIntent().getSerializableExtra("market");
    }
}
