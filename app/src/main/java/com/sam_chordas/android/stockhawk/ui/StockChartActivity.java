package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.Arrays;

/**
 * Created by Ashwini on 4/3/2016.
 */
public class StockChartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        LineChartView lineChartView = (LineChartView) findViewById(R.id.linechart);

        String symbol = getIntent().getStringExtra("symbol");
        Cursor c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.BIDPRICE}, QuoteColumns.SYMBOL + "= ?",
                new String[]{symbol}, null);

        LineSet dataset = new LineSet();

        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int id = c.getInt(c.getColumnIndex(QuoteColumns._ID));
                String bidValue = c.getString(c.getColumnIndex(QuoteColumns.BIDPRICE));
                dataset.addPoint(new Point("", Float.valueOf(bidValue)));
                c.moveToNext();
            }
        }

        dataset.setColor(Color.BLUE);

        lineChartView.setStep(10);
        lineChartView.setBackgroundColor(Color.WHITE);
        lineChartView.addData(dataset);
        lineChartView.show();

        c.close();
    }

}
