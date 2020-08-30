package com.example.proyectopst;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TablaDinamica {
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC;
    private int indexR;

    public TablaDinamica(TableLayout tableLayout, Context context){
        this.tableLayout = tableLayout;
        this.context = context;
    }

    public void addHeader(String[] header){
        this.header = header;
        createHeader();
    }

    public void addData(ArrayList<String[]> data){
        this.data = data;
        createDataTable();
    }

    private void newRow(){
        tableRow = new TableRow(context);
    }

    private void newCell(){
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(12);
    }

    private void createHeader(){
        indexC = 0;
        newRow();
        for(String datos: header){
            newCell();
            txtCell.setText(datos);
            txtCell.setBackgroundColor(Color.parseColor("#7992CF"));
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    private void createDataTable(){
        for(String[] datos2: data) {
            newRow();
            for (String datos3: datos2) {
                newCell();
                txtCell.setText(datos3);
                txtCell.setBackgroundColor(Color.parseColor("#C3D1F3"));
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    public TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight = 1;
        return params;
    }
}
