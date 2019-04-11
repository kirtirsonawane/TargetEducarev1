package com.targeteducare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.targeteducare.Adapter.ItemClickListener;
import com.targeteducare.Adapter.Section;
import com.targeteducare.Adapter.SectionedExpandableLayoutHelper;
import com.targeteducare.Classes.Item;
import com.targeteducare.Classes.Question;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this, mRecyclerView, this, 3,false);

        ArrayList<Item> arrayList = new ArrayList<>();
        arrayList.add(new Item("08:00 AM", 0));
        arrayList.add(new Item("10:00 AM", 1));
        arrayList.add(new Item("01:00 PM", 2));
        arrayList.add(new Item("05:00 PM", 3));
        //sectionedExpandableLayoutHelper.addSection(new Item(), arrayList);
        arrayList = new ArrayList<>();
        arrayList.add(new Item("08:00 AM", 0));
        arrayList.add(new Item("10:00 AM", 1));
        arrayList.add(new Item("01:00 PM", 2));
        arrayList.add(new Item("05:00 PM", 3));
       // sectionedExpandableLayoutHelper.addSection(new Item(), arrayList);
        arrayList = new ArrayList<>();
        arrayList.add(new Item("08:00 AM", 0));
        arrayList.add(new Item("10:00 AM", 1));
        arrayList.add(new Item("01:00 PM", 2));
        arrayList.add(new Item("05:00 PM", 3));
      //  sectionedExpandableLayoutHelper.addSection(new Item(), arrayList);

        sectionedExpandableLayoutHelper.notifyDataSetChanged();

        //checking if adding single item works
        //sectionedExpandableLayoutHelper.addItem("CIVILWAR (U/A) ENGLISH", new Item("06:30 PM",5));
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }



    @Override
    public void itemClicked(Question item) {

    }

    @Override
    public void itemClicked(Section section) {

    }


/*    @Override
    public void itemClicked(Item item) {
        Toast.makeText(this, "Item: " + item.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }*/

   /* @Override
    public void itemClicked(Item item) {

    }

    @Override
    public void itemClicked(Section section) {
        Toast.makeText(this, "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }*/

}
