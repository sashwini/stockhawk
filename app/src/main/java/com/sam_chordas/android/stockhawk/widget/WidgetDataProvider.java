package com.sam_chordas.android.stockhawk.widget;

/**
 * Created by Ashwini on 6/15/2016.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory {

    Cursor stockCursor;
    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;

        initData();
    }

    @Override
    public int getCount() {
        return stockCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        stockCursor.moveToPosition(position);
        mView.setTextViewText(R.id.symbol_text_view,
                stockCursor.getString(stockCursor.getColumnIndex(QuoteColumns.SYMBOL)));
        mView.setTextViewText(R.id.price_text_view,
                stockCursor.getString(stockCursor.getColumnIndex(QuoteColumns.BIDPRICE)));

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(StockWidgetProvider.ACTION_CLICK);
        final Bundle bundle = new Bundle();
        bundle.putString(StockWidgetProvider.EXTRA_POSITION,
                String.valueOf(position));
        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(R.id.symbol_text_view, fillInIntent);
        mView.setOnClickFillInIntent(R.id.price_text_view, fillInIntent);
        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
    }

    private void initData() {
        stockCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"}, null);
        if (stockCursor == null) {
            return;
        }
        stockCursor.moveToFirst();
    }

    @Override
    public void onDestroy() {

    }

}
