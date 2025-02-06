package edu.jsu.mcis.cs408.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final int VERT_CHAIN = 4, HORI_CHAIN = 5;
    private String[] textStrings = new String[20], tagStrings = new String[20];
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        textStrings = getResources().getStringArray(R.array.textStrings);
        tagStrings = getResources().getStringArray(R.array.tagStrings);

        initLayout();
    }

    public void onClick(View v) {

    }

    private void initLayout() {

        int[][] buttonIdsVert = new int[HORI_CHAIN][VERT_CHAIN];
        int[][] buttonIdsHori = new int[VERT_CHAIN][HORI_CHAIN];

        ConstraintLayout layout = binding.getRoot();
        ViewGroup.LayoutParams params;
        ViewGroup.MarginLayoutParams marg;

        Log.i("MainActivity", "screaming");

        int tvId = View.generateViewId();
        TextView tv = new TextView(this);
        tv.setId(tvId);
        tv.setTag("tv");
        tv.setText("0");
        tv.setTextSize(36);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        layout.addView(tv);


        params = tv.getLayoutParams();
        params.width = 0;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tv.setLayoutParams(params);





        int k = 0;
        for(int i = 0; i < 4; i++) {;
            for(int j = 0; j < 5; j++) {
                //Log.i("MainActivity", "We made it here");
                int btnId = View.generateViewId();
                Log.i("MainActivity", String.valueOf(btnId));
                //Log.i("MainActivity", "Now we are here");
                Button btn = new Button(this);
                btn.setId(btnId);
                btn.setTag(tagStrings[k]);
                btn.setText(textStrings[k]);
                btn.setTextSize(24);
                btn.setWidth(0);
                btn.setHeight(0);
                layout.addView(btn);

                params = btn.getLayoutParams();
                marg = (ViewGroup.MarginLayoutParams) params;
                marg.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                marg.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                marg.setMargins(6,6,6,6);
                btn.setLayoutParams(marg);


                buttonIdsVert[j][i] = btnId;
                //buttonIdsHori[j][i] = buttonIdsVert[i][j];
                k++;
            }
        }


        Log.i("MainActivity", "WOAHHHHHHHHHHHHH");
        Log.i("MainActivity", buttonIdsVert.toString());

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        // non-chain constraints
        set.connect(tv.getId(), ConstraintSet.RIGHT, binding.guideEast.getId(), ConstraintSet.RIGHT);
        set.connect(tv.getId(), ConstraintSet.LEFT, binding.guideWest.getId(), ConstraintSet.LEFT);
        set.connect(tv.getId(), ConstraintSet.TOP, binding.guideNorth.getId(), ConstraintSet.BOTTOM);


        // vert chain
        for(int i = 0; i < HORI_CHAIN; i++) {
            Log.i("MainActivity", "Yippee");
            set.createVerticalChain(tv.getId(), ConstraintSet.BOTTOM, binding.guideSouth.getId(),
                    ConstraintSet.TOP, buttonIdsVert[i], null, ConstraintSet.CHAIN_PACKED);
            set.applyTo(layout);
        }



        // hori chain
        for(int i = 0; i < VERT_CHAIN; i++) {
            int tempChain[] = new int[5];
            for(int j = 0; j < HORI_CHAIN; j++) {
                tempChain[j] = buttonIdsVert[j][i];
            }
            set.createHorizontalChain(binding.guideWest.getId(), ConstraintSet.RIGHT, binding.guideEast.getId(),
                    ConstraintSet.LEFT, tempChain, null, ConstraintSet.CHAIN_PACKED);
            set.applyTo(layout);
        }
        // set.createHorizontalChain();


    }
}