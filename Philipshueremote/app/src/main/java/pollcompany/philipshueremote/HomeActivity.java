package pollcompany.philipshueremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private List<Lamp> lamps;
    private RecyclerView recyclerViewLamps;
    private RecyclerView.Adapter lampAdapter;
    private RecyclerView.LayoutManager lampLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        buildArrayList();

        recyclerViewLamps = (RecyclerView) findViewById(R.id.recyclerViewLamps);
        lampLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewLamps.setHasFixedSize(false);
        recyclerViewLamps.setLayoutManager(lampLayoutManager);

    }

    private void buildArrayList() {
        lamps = new ArrayList<>();
        lamps.add(new Lamp("1", 0, 0, 0));
        lamps.add(new Lamp("4", 2, 0, 4));
    }
}
