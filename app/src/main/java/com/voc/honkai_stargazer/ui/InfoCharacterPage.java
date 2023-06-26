/*
 * Project Honkai Stargazer (崩壞•星穹觀星者) was
 * Created & Develop by Voc-夜芷冰 , Programmer of Xectorda
 * Copyright © 2023 Xectorda 版權所有
 */

package com.voc.honkai_stargazer.ui;


import static com.voc.honkai_stargazer.util.ItemRSS.LoadAssestData;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.voc.honkai_stargazer.R;
import com.voc.honkai_stargazer.data.HSRItem;
import com.voc.honkai_stargazer.data.MaterialItem;
import com.voc.honkai_stargazer.util.CustomViewPagerAdapter;
import com.voc.honkai_stargazer.util.ItemRSS;
import com.voc.honkai_stargazer.util.LangUtil;
import com.voc.honkai_stargazer.util.LogExport;
import com.voc.honkai_stargazer.util.ThemeUtil;
import com.voc.honkai_stargazer.util.VibrateUtil;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InfoCharacterPage {
    Context context;
    Activity activity;
    HSRItem hsrItem;

    ViewPager info_vp;
    View info_introducing, info_combat, info_eidolon, info_advice, info_trace;
    ArrayList<View> viewArrayList = new ArrayList<>();
    TabLayout info_tablayout;

    public static final int TAB_INTRODUCING = 100;
    public static final int TAB_COMBAT = 101;
    public static final int TAB_EIDOLON = 102;
    public static final int TAB_ADVICE = 103;
    public static final int TAB_TRACE = 104;

    public static final String CHOICE_OR = " / ";
    public static final String CHOICE_BEST = " >>> ";
    public static final String CHOICE_BETTER = " > ";

    ItemRSS item_rss;

    ArrayList<MaterialItem> materialItemsRef = new ArrayList<>();
    ArrayList<Integer> materialItemsID = new ArrayList<>();
    public static final String TAG = "InfoCharacterPage";

    SharedPreferences sharedPreferences;

    ThemeUtil themeUtil;

    ArrayList<HSRItem> lightconesList = new ArrayList<>();
    ArrayList<String> lightconesNameList = new ArrayList<>();

    public void setup(Context context, Activity activity, HSRItem hsrItem){
        this.context = context;
        this.activity = activity;
        this.hsrItem = hsrItem;
        item_rss = new ItemRSS();
        sharedPreferences = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);

        final LayoutInflater mInflater = activity.getLayoutInflater().from(context);
        info_introducing = mInflater.inflate(R.layout.fragment_info_character_intro, null,false);
        info_combat = mInflater.inflate(R.layout.fragment_info_character_combat, null,false);
        info_eidolon = mInflater.inflate(R.layout.fragment_info_character_eidolon, null,false);
        info_advice = mInflater.inflate(R.layout.fragment_info_character_advice, null,false);

        viewArrayList = new ArrayList<>();
        viewArrayList.add(info_introducing);
        viewArrayList.add(info_combat);
        viewArrayList.add(info_eidolon);
        viewArrayList.add(info_advice);

        final Dialog dialog = new Dialog(context, R.style.PageDialogStyle_P);
        View view = View.inflate(context, R.layout.fragment_info_character_root, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight()));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        themeUtil = new ThemeUtil(context,activity);
        themeUtil.navigationSetup(activity.getWindow());
        themeUtil.themeTint(
                view.findViewById(R.id.rootView_info_character),
                info_introducing.findViewById(R.id.rootView_info_character_intro),
                info_combat.findViewById(R.id.rootView_info_character_combat),
                info_eidolon.findViewById(R.id.rootView_info_character_eidolon),
                info_advice.findViewById(R.id.rootView_info_character_advice)
        );

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);

        ImageButton info_back = view.findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        info_vp = view.findViewById(R.id.info_vp);
        info_vp.setAdapter(new CustomViewPagerAdapter(viewArrayList));
        info_tablayout = view.findViewById(R.id.info_tablayout);

        info_tablayout.addTab(info_tablayout.newTab().setId(TAB_INTRODUCING).setText(R.string.character_intro));
        info_tablayout.addTab(info_tablayout.newTab().setId(TAB_COMBAT).setText(R.string.character_combat));
        info_tablayout.addTab(info_tablayout.newTab().setId(TAB_EIDOLON).setText(R.string.character_eidolon));
        info_tablayout.addTab(info_tablayout.newTab().setId(TAB_ADVICE).setText(R.string.character_advice));


        info_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                info_vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        info_vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(info_tablayout));

        lightcone_list_reload();

        String LANGUAGE = ItemRSS.initLang(context).getCode();
        //Read JSON from Assests
        String json_base = LoadAssestData(context,"character_data/"+LANGUAGE+"/"+hsrItem.getFileName()+".json");
        String json_base2 = LoadAssestData(context,"character_data/"+LangUtil.LangType.EN.getCode()+"/"+hsrItem.getFileName()+".json");
        String json_advice = LoadAssestData(context, "character_data/advice/" +hsrItem.getFileName()+".json");
        System.out.println("JSON : "+ "character_data/advice/" +hsrItem.getFileName()+".json");

        try {
            init_advice(json_advice);
        } catch (JSONException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogExport.bugLog(TAG, "Read JSON from Assests - advice", sw.toString(), context);
        }

        if (json_base == "" && json_base2 != ""){json_base  =json_base2;}
        if (json_base != ""){
            try {
                JSONObject jsonObject = new JSONObject(json_base);
                TextView info_char_name = view.findViewById(R.id.info_char_name);
                info_char_name.setText(jsonObject.getString("name"));
                info_char_name.getPaint().setFakeBoldText(true);

                itemReferences_init(jsonObject);
                init_intro(jsonObject);
                init_combat(jsonObject);
                init_eidolon(jsonObject);
                //help_tool_eidolon(jsonObject);
                //help_tool_material(jsonObject);
                //help_tool_skill(jsonObject, hsrItem.getName());

                if (!dialog.isShowing()){
                    dialog.show();
                }

            } catch (JSONException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                LogExport.bugLog(TAG, "Read JSON from Assests", sw.toString(), context);
            }
        }else{
            Toast.makeText(context, "["+LANGUAGE+"] "+hsrItem.getName()+"'s file not exist", Toast.LENGTH_SHORT).show();
        }

    }

    private void init_advice(String json_advice) throws JSONException{
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        System.out.println("json_advice : aa"+json_advice+"bb");
        if (json_advice.equals("")){
            info_tablayout.removeTabAt(info_tablayout.getTabCount()-1);
        }else{
            JSONObject jsonAdvice = new JSONObject(json_advice);

            // Relic
            JSONArray relics = jsonAdvice.getJSONArray("relics");
            LinearLayout advice_relic_set_ll = info_advice.findViewById(R.id.advice_relic_set_ll);

            String tmpBody = "";
            String tmpFeet = "";
            TextView advice_relic_body_name = advice_relic_set_ll.findViewById(R.id.advice_relic_body_name);
            TextView advice_relic_feet_name = advice_relic_set_ll.findViewById(R.id.advice_relic_feet_name);
            JSONArray body = jsonAdvice.getJSONArray("body");
            JSONArray feet = jsonAdvice.getJSONArray("feet");
            for (int x = 0 ; x < body.length() ; x++){tmpBody += item_rss.localeStatusFromPrydwen(body.getJSONObject(x).getString("stat"),context) + (x+1 < body.length() ? CHOICE_OR : "");}
            for (int x = 0 ; x < feet.length() ; x++){tmpFeet += item_rss.localeStatusFromPrydwen(feet.getJSONObject(x).getString("stat"),context) + (x+1 < feet.length() ? CHOICE_OR : "");}
            advice_relic_body_name.setText(tmpBody);
            advice_relic_feet_name.setText(tmpFeet);

            for (int x = 0 ; x < relics.length() ; x++){
                JSONObject relic_set = relics.getJSONObject(x);
                String relic = relic_set.getString("relic");
                String relic_2 = relic_set.getString("relic_2");

                View relicItem = View.inflate(context, R.layout.item_advice_relic, null);
                ImageView advice_relic_set_icon = relicItem.findViewById(R.id.advice_relic_set_icon);
                ImageView advice_relic_set_icon2 = relicItem.findViewById(R.id.advice_relic_set_icon2);
                TextView advice_relic_set_name = relicItem.findViewById(R.id.advice_relic_set_name);
                TextView advice_relic_set_name2 = relicItem.findViewById(R.id.advice_relic_set_name2);
                TextView advice_relic_set_2pc_title = relicItem.findViewById(R.id.advice_relic_set_2pc_title);
                TextView advice_relic_set_4pc_title = relicItem.findViewById(R.id.advice_relic_set_4pc_title);
                TextView advice_relic_set_2pc = relicItem.findViewById(R.id.advice_relic_set_2pc);
                TextView advice_relic_set_4pc = relicItem.findViewById(R.id.advice_relic_set_4pc);
                LinearLayout advice_relic_set_info = relicItem.findViewById(R.id.advice_relic_set_info);
                ImageButton advice_relic_set_btn = relicItem.findViewById(R.id.advice_relic_set_btn);

                advice_relic_set_name.setText(item_rss.getLocalNameByName(relic,context));
                advice_relic_set_2pc.setText(item_rss.getRelicStatusByName(relic,context)[0]);
                advice_relic_set_4pc.setText(item_rss.getRelicStatusByName(relic,context)[1]);

                // 2x2pc
                if (!relic_2.equals("") && relic_2 != null){
                    advice_relic_set_2pc_title.setText(context.getString(R.string.relic_2pc)+" ("+item_rss.getLocalNameByName(relic,context)+")");
                    advice_relic_set_4pc_title.setText(context.getString(R.string.relic_2pc)+" ("+item_rss.getLocalNameByName(relic_2,context)+")");
                    advice_relic_set_name2.setText(item_rss.getLocalNameByName(relic_2,context));

                    advice_relic_set_icon.getLayoutParams().width = (int) (48 * displayMetrics.density);
                    advice_relic_set_icon.getLayoutParams().height = (int) (48 * displayMetrics.density);
                    Picasso.get()
                            .load(item_rss.getRelicByName(relic)[0])
                            .into(advice_relic_set_icon);

                    advice_relic_set_icon2.getLayoutParams().width = (int) (48 * displayMetrics.density);
                    advice_relic_set_icon2.getLayoutParams().height = (int) (48 * displayMetrics.density);
                    Picasso.get()
                            .load(item_rss.getRelicByName(relic_2)[0])
                            .into(advice_relic_set_icon2);
                }else{
                    advice_relic_set_icon.getLayoutParams().width = (int) (64 * displayMetrics.density);
                    advice_relic_set_icon.getLayoutParams().height = (int) (64 * displayMetrics.density);
                    Picasso.get()
                            .load(item_rss.getRelicByName(relic)[0])
                            .into(advice_relic_set_icon);

                    advice_relic_set_icon2.setVisibility(View.GONE);
                    advice_relic_set_name2.setVisibility(View.GONE);
                }

                advice_relic_set_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (advice_relic_set_info.getVisibility() == View.VISIBLE){
                            Animation ani = new ShowAnim(advice_relic_set_info,false);
                            ani.setDuration(250);
                            ani.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    advice_relic_set_info.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            advice_relic_set_info.startAnimation(ani);
                            ImageViewAnimatedChange(context, advice_relic_set_btn, R.drawable.ic_arrow_show_btn);
                        }else{
                            Animation ani = new ShowAnim(advice_relic_set_info,true);
                            ani.setDuration(250);
                            advice_relic_set_info.setVisibility(View.VISIBLE);
                            advice_relic_set_info.startAnimation(ani);
                            ImageViewAnimatedChange(context, advice_relic_set_btn, R.drawable.ic_arrow_hide_btn);
                        }
                    }
                });
                advice_relic_set_ll.addView(relicItem, 0);
            }

            // Planetary Planar
            JSONArray planars = jsonAdvice.getJSONArray("planars");
            LinearLayout advice_planetary_planar_set_ll = info_advice.findViewById(R.id.advice_planetary_planar_set_ll);

            String tmpRope = "";
            String tmpSphere = "";
            TextView advice_planetary_planar_sphere_name = advice_planetary_planar_set_ll.findViewById(R.id.advice_planetary_planar_sphere_name);
            TextView advice_planetary_link_rope_name = advice_planetary_planar_set_ll.findViewById(R.id.advice_planetary_link_rope_name);
            JSONArray rope = jsonAdvice.getJSONArray("rope");
            JSONArray sphere = jsonAdvice.getJSONArray("sphere");
            for (int x = 0 ; x < rope.length() ; x++){tmpRope += item_rss.localeStatusFromPrydwen(rope.getJSONObject(x).getString("stat"),context) + (x+1 < rope.length() ? CHOICE_OR : "");}
            for (int x = 0 ; x < sphere.length() ; x++){tmpSphere += item_rss.localeStatusFromPrydwen(sphere.getJSONObject(x).getString("stat"),context) + (x+1 < sphere.length() ? CHOICE_OR : "");}
            advice_planetary_link_rope_name.setText(tmpRope);
            advice_planetary_planar_sphere_name.setText(tmpSphere);

            for (int x = 0 ; x < planars.length() ; x++){
                JSONObject planar_set = planars.getJSONObject(x);
                String planar = planar_set.getString("planar");

                View relicItem = View.inflate(context, R.layout.item_advice_relic, null);
                ImageView advice_relic_set_icon = relicItem.findViewById(R.id.advice_relic_set_icon);
                ImageView advice_relic_set_icon2 = relicItem.findViewById(R.id.advice_relic_set_icon2);
                TextView advice_relic_set_name = relicItem.findViewById(R.id.advice_relic_set_name);
                TextView advice_relic_set_name2 = relicItem.findViewById(R.id.advice_relic_set_name2);
                LinearLayout advice_relic_set_info = relicItem.findViewById(R.id.advice_relic_set_info);
                ImageButton advice_relic_set_btn = relicItem.findViewById(R.id.advice_relic_set_btn);
                TextView advice_relic_set_2pc_title = relicItem.findViewById(R.id.advice_relic_set_2pc_title);
                TextView advice_relic_set_4pc_title = relicItem.findViewById(R.id.advice_relic_set_4pc_title);
                TextView advice_relic_set_2pc = relicItem.findViewById(R.id.advice_relic_set_2pc);
                TextView advice_relic_set_4pc = relicItem.findViewById(R.id.advice_relic_set_4pc);

                advice_relic_set_icon.getLayoutParams().width = (int) (64 * displayMetrics.density);
                advice_relic_set_icon.getLayoutParams().height = (int) (64 * displayMetrics.density);
                Picasso.get()
                        .load(item_rss.getRelicByName(planar)[0])
                        .fit()
                        .into(advice_relic_set_icon);
                advice_relic_set_name.setText(item_rss.getLocalNameByName(planar,context));
                advice_relic_set_2pc.setText(item_rss.getRelicStatusByName(planar,context)[0]);

                advice_relic_set_icon2.setVisibility(View.GONE);
                advice_relic_set_name2.setVisibility(View.GONE);
                advice_relic_set_4pc.setVisibility(View.GONE);
                advice_relic_set_4pc_title.setVisibility(View.GONE);
                advice_relic_set_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (advice_relic_set_info.getVisibility() == View.VISIBLE){
                            Animation ani = new ShowAnim(advice_relic_set_info,false);
                            ani.setDuration(250);
                            ani.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    advice_relic_set_info.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            advice_relic_set_info.startAnimation(ani);
                            ImageViewAnimatedChange(context, advice_relic_set_btn, R.drawable.ic_arrow_show_btn);
                        }else{
                            Animation ani = new ShowAnim(advice_relic_set_info,true);
                            ani.setDuration(250);
                            advice_relic_set_info.setVisibility(View.VISIBLE);
                            advice_relic_set_info.startAnimation(ani);
                            ImageViewAnimatedChange(context, advice_relic_set_btn, R.drawable.ic_arrow_hide_btn);
                        }
                    }
                });

                advice_planetary_planar_set_ll.addView(relicItem, 0);
            }

            // Lightcone
            JSONArray lightcone = jsonAdvice.getJSONArray("cones");
            LinearLayout advice_lightcone_ll = info_advice.findViewById(R.id.advice_lightcone_ll);
            for (int x = 0 ; x < lightcone.length() ; x ++){
                View lightconeItem = View.inflate(context, R.layout.item_advice_lightcone, null);
                ImageView advicve_lightcone_img = lightconeItem.findViewById(R.id.advicve_lightcone_img);
                TextView advicve_lightcone_name = lightconeItem.findViewById(R.id.advicve_lightcone_name);
                TextView advicve_lightcone_lvl = lightconeItem.findViewById(R.id.advicve_lightcone_lvl);
                ImageView advice_lightcone_star = lightconeItem.findViewById(R.id.advice_lightcone_star);

                Picasso.get()
                        .load(item_rss.getLightconeByName(item_rss.getLocaleNameByPrydwen(lightcone.getJSONObject(x).getString("cone"), context))[0])
                        .error(R.drawable.ico_lost_img)
                        .into(advicve_lightcone_img);
                advicve_lightcone_name.setText(item_rss.getLocalNameByName(item_rss.getLocaleNameByPrydwen(lightcone.getJSONObject(x).getString("cone"), context), context));
                advicve_lightcone_lvl.setText(String.valueOf(lightconesList.get(lightconesNameList.indexOf(
                        item_rss.getLocaleNameByPrydwen(lightcone.getJSONObject(x).getString("cone"), context)
                )).getRare()));

                Drawable drawable = context.getDrawable(R.drawable.ic_rare_star);
                drawable.setTint(Color.parseColor(sharedPreferences.getString("themedColor","#6750A4")));
                advice_lightcone_star.setBackground(drawable);
                advice_lightcone_ll.addView(lightconeItem);
            }

            themeUtil.themeTint(
                    info_advice.findViewById(R.id.rootView_info_character_advice)
            );
        }
    }
    public class ShowAnim extends Animation {
        View view;
        boolean isPushDownAnim;
        int targetHeight;
        int targetTop;
        int targetTopLast;

        public ShowAnim(View view, boolean isPushDownAnim) {
            this.view = view;
            this.isPushDownAnim = isPushDownAnim;
            targetHeight = view.getMeasuredHeight();
            if (isPushDownAnim){
                targetTop = 0;
                targetTopLast = -view.getMeasuredHeight();
            }else{
                targetTop = -view.getMeasuredHeight();
                targetTopLast = 0;
            }

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float interpolatedTime_auth = interpolatedTime;
            if (isPushDownAnim){
                interpolatedTime_auth = (1-interpolatedTime);
            }

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(0, (int)  ((targetTopLast + targetTop)*interpolatedTime_auth),0,0);
            view.setLayoutParams(layoutParams);
            view.setAlpha(1-interpolatedTime_auth*2);

        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private void itemReferences_init(JSONObject jsonObject) throws JSONException {
        materialItemsRef = new ArrayList<>();
        ArrayList<MaterialItem> materialItemsRefPRE = new ArrayList<>();
        ArrayList<MaterialItem> currency = new ArrayList<>();
        ArrayList<MaterialItem> exp = new ArrayList<>();
        JSONObject itemReferences = jsonObject.getJSONObject("itemReferences");
        Iterator<String> item_iter = itemReferences.keys();
        while (item_iter.hasNext()){
            String materialKey = item_iter.next();
            MaterialItem materialItem = new MaterialItem();
            materialItem.setId(itemReferences.getJSONObject(materialKey).getInt("id"));
            materialItem.setType(itemReferences.getJSONObject(materialKey).getInt("type"));
            materialItem.setPurposeId(itemReferences.getJSONObject(materialKey).getInt("purposeId"));
            materialItem.setName(itemReferences.getJSONObject(materialKey).getString("name"));
            materialItem.setDesc(itemReferences.getJSONObject(materialKey).getString("desc"));
            materialItem.setLore(itemReferences.getJSONObject(materialKey).getString("lore"));
            materialItem.setPurpose(itemReferences.getJSONObject(materialKey).getString("purpose"));
            materialItem.setRarity(itemReferences.getJSONObject(materialKey).getInt("rarity"));

            ArrayList<String> comeFrom = new ArrayList<>();
            for (int x = 0 ; x < itemReferences.getJSONObject(materialKey).getJSONArray("comeFrom").length() ; x++){
                comeFrom.add(String.valueOf(itemReferences.getJSONObject(materialKey).getJSONArray("comeFrom").get(x)));
            }
            materialItem.setComeFrom(comeFrom);
            if (materialItem.getPurposeId() == ItemRSS.MATERIAL_COMMON_CURRENCY){
                currency.add(materialItem);
            }else if (materialItem.getPurposeId() == ItemRSS.MATERIAL_CHARACTER_EXP_MATERIAL){
                exp.add(materialItem);
            }else{
                materialItemsRefPRE.add(materialItem);
            }
        }

        //https://stackoverflow.com/questions/9109890/android-java-how-to-sort-a-list-of-objects-by-a-certain-value-within-the-object
        Collections.sort(materialItemsRefPRE, new Comparator<MaterialItem>(){
            public int compare(MaterialItem obj1, MaterialItem obj2) {
                // ## Ascending order
                //return obj1.getId().compareToIgnoreCase(obj2.firstName); // To compare string values
                return Integer.valueOf(obj1.getRarity()).compareTo(Integer.valueOf(obj2.getRarity())); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
        Collections.sort(exp, new Comparator<MaterialItem>(){
            public int compare(MaterialItem obj1, MaterialItem obj2) {
                return Integer.valueOf(obj1.getRarity()).compareTo(Integer.valueOf(obj2.getRarity()));
            }
        });
        Collections.sort(currency, new Comparator<MaterialItem>(){
            public int compare(MaterialItem obj1, MaterialItem obj2) {
                return Integer.valueOf(obj1.getRarity()).compareTo(Integer.valueOf(obj2.getRarity()));
            }
        });
        materialItemsRef.addAll(currency);
        materialItemsRef.addAll(exp);
        materialItemsRef.addAll(materialItemsRefPRE);
    }

    private void init_intro(JSONObject jsonObject) throws JSONException {
        ImageView intro_ico = info_introducing.findViewById(R.id.intro_ico);
        ImageView intro_path_ico = info_introducing.findViewById(R.id.intro_path_ico);
        ImageView intro_element_ico = info_introducing.findViewById(R.id.intro_element_ico);
        TextView intro_path_tv = info_introducing.findViewById(R.id.intro_path_tv);
        TextView intro_element_tv = info_introducing.findViewById(R.id.intro_element_tv);
        TextView intro_desc = info_introducing.findViewById(R.id.intro_desc);
        ScaleRatingBar intro_rare = info_introducing.findViewById(R.id.intro_rare);

        intro_ico.setImageResource(item_rss.getCharByName(hsrItem.getName())[1]);
        intro_path_ico.setImageResource(item_rss.getIconByPath(hsrItem.getPath()));
        intro_element_ico.setImageResource(item_rss.getIconByElement(hsrItem.getElement()));
        intro_path_tv.setText(item_rss.getNameByPath(hsrItem.getPath()));
        intro_path_tv.getPaint().setFakeBoldText(true);
        intro_element_tv.setText(item_rss.getNameByElement(hsrItem.getElement()));
        intro_rare.setNumStars(hsrItem.getRare());
        intro_rare.setRating(hsrItem.getRare());
        intro_desc.setText(jsonObject.getString("descHash"));

        LinearLayout intro_ll_1 = info_introducing.findViewById(R.id.intro_ll_1);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    }

    private void init_combat(JSONObject jsonObject) throws JSONException {
        //Status Card
        TextView combat_status_hp = info_combat.findViewById(R.id.combat_status_hp);
        TextView combat_status_atk = info_combat.findViewById(R.id.combat_status_atk);
        TextView combat_status_def = info_combat.findViewById(R.id.combat_status_def);
        TextView combat_status_spd = info_combat.findViewById(R.id.combat_status_spd);
        TextView combat_status_taunt = info_combat.findViewById(R.id.combat_status_taunt);
        LinearLayout combat_status_material_ll = info_combat.findViewById(R.id.combat_status_material_ll);
        SeekBar combat_status_seekbar = info_combat.findViewById(R.id.combat_status_seekbar);
        TextView combat_status_lv = info_combat.findViewById(R.id.combat_status_lv);

        LinearLayout combat_ll_0 = info_combat.findViewById(R.id.combat_ll_0);
        LinearLayout combat_ll_1 = info_combat.findViewById(R.id.combat_ll_1);
        LinearLayout combat_ll_2 = info_combat.findViewById(R.id.combat_ll_2);
        LinearLayout combat_ll_3 = info_combat.findViewById(R.id.combat_ll_3);
        LinearLayout combat_ll_4 = info_combat.findViewById(R.id.combat_ll_4);
        LinearLayout combat_ll_6 = info_combat.findViewById(R.id.combat_ll_6);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        combat_status_seekbar.setProgress(0);
        combat_status_seekbar.setMax(80-1+6);
        combat_material_init(combat_status_material_ll, jsonObject, -1);
        combat_status_change(jsonObject, combat_status_seekbar, 0, combat_status_hp, combat_status_atk, combat_status_def, combat_status_spd, combat_status_taunt);
        combat_status_lv.setText("01/20");

        combat_status_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                VibrateUtil.vibrate(context);
                try {
                    int lvlCurr = seekBar.getProgress()+1;
                    int lvlPART = 0;
                    int[] lvlMax = new int[]{20,30,40,50,60,70,80};
                    for (int x = 0 ; x < lvlMax.length ; x ++){
                        if (seekBar.getProgress()+1 - lvlPART > lvlMax[x]){
                            lvlPART ++;
                        }
                    }

                    lvlCurr = seekBar.getProgress() + 1 - lvlPART;

                    combat_material_change(combat_status_material_ll, jsonObject, lvlPART-1);
                    combat_status_change(jsonObject, seekBar, lvlPART, combat_status_hp, combat_status_atk, combat_status_def, combat_status_spd, combat_status_taunt);
                    combat_status_lv.setText((lvlCurr < 10 ? "0" : "")+String.valueOf(lvlCurr)+" / "+String.valueOf(lvlMax[lvlPART]));
                } catch (JSONException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LogExport.bugLog(TAG, "combat_material_change | combat_status_change", sw.toString(), context);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Combat Card
        int[] combat_ico = new int[]{R.id.combat_skill1_ico,R.id.combat_skill2_ico,R.id.combat_skill3_ico,R.id.combat_skill4_ico,R.id.combat_skill6_ico};
        int[] combat_desc = new int[]{R.id.combat_skill1_desc,R.id.combat_skill2_desc,R.id.combat_skill3_desc,R.id.combat_skill4_desc,R.id.combat_skill6_desc};
        int[] combat_name = new int[]{R.id.combat_skill1_name,R.id.combat_skill2_name,R.id.combat_skill3_name,R.id.combat_skill4_name,R.id.combat_skill6_name};
        int[] combat_type = new int[]{R.id.combat_skill1_type,R.id.combat_skill2_type,R.id.combat_skill3_type,R.id.combat_skill4_type,R.id.combat_skill6_type};
        int[] combat_material_ll = new int[]{R.id.combat_skill1_material_ll,R.id.combat_skill2_material_ll,R.id.combat_skill3_material_ll,R.id.combat_skill4_material_ll,R.id.combat_skill6_material_ll};
        int[] combat_lv = new int[]{R.id.combat_skill1_lv,R.id.combat_skill2_lv,R.id.combat_skill3_lv,R.id.combat_skill4_lv,R.id.combat_skill6_lv};
        int[] combat_seekbar = new int[]{R.id.combat_skill1_seekbar,R.id.combat_skill2_seekbar,R.id.combat_skill3_seekbar,R.id.combat_skill4_seekbar,R.id.combat_skill6_seekbar};

        for (int x = 0 ; x < 5 ; x++){
            JSONArray skills = jsonObject.getJSONArray("skills");

            ImageView ico = info_combat.findViewById(combat_ico[x]);
            TextView desc = info_combat.findViewById(combat_desc[x]);
            TextView name = info_combat.findViewById(combat_name[x]);
            TextView type = info_combat.findViewById(combat_type[x]);
            TextView lv = info_combat.findViewById(combat_lv[x]);
            LinearLayout material_ll = info_combat.findViewById(combat_material_ll[x]);
            SeekBar seekbar = info_combat.findViewById(combat_seekbar[x]);

            seekbar.setProgress(0);
            seekbar.setMax(1);

            int finalX = (x == 4 ? 5 : x);
            ico.setImageResource(item_rss.getCharSkillByName(hsrItem.getName())[x]);
            name.setText(skills.getJSONObject(finalX).getString("name"));
            type.setText(skills.getJSONObject(finalX).getString("typeDescHash"));
            combat_desc_change(desc, skills.getJSONObject(finalX), seekbar);
            combat_material_init(material_ll, skills.getJSONObject(finalX), finalX);
            combat_material_change(material_ll, skills.getJSONObject(finalX), -1);
            lv.setText("01");

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    VibrateUtil.vibrate(context);
                    try {
                        combat_material_change(material_ll, skills.getJSONObject(finalX), seekBar.getProgress());
                        combat_desc_change(desc, skills.getJSONObject(finalX), seekBar);
                        lv.setText((seekBar.getProgress()+1 < 10 ? "0" : "")+String.valueOf(seekBar.getProgress()+1));
                    } catch (JSONException e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        LogExport.bugLog(TAG, "combat_material_change | combat_desc_change", sw.toString(), context);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });


        }

    }

    private void combat_status_change(JSONObject jsonObject, SeekBar seekBar,  int lvlPART, TextView combat_status_hp, TextView combat_status_atk, TextView combat_status_def, TextView combat_status_spd, TextView combat_status_taunt) throws JSONException {
        if (jsonObject.has("levelData")) {
            JSONArray levelData = jsonObject.getJSONArray("levelData");

            DecimalFormat df = ItemRSS.getDecimalFormat();

            combat_status_hp.setText(df.format(levelData.getJSONObject(lvlPART).getDouble("hpBase") + levelData.getJSONObject(lvlPART).getDouble("hpAdd") * (seekBar.getProgress() - lvlPART)));
            combat_status_atk.setText(df.format(levelData.getJSONObject(lvlPART).getDouble("attackBase") + levelData.getJSONObject(lvlPART).getDouble("attackAdd") * (seekBar.getProgress() - lvlPART)));
            combat_status_def.setText(df.format(levelData.getJSONObject(lvlPART).getDouble("defenseBase") + levelData.getJSONObject(lvlPART).getDouble("defenseAdd") * (seekBar.getProgress() - lvlPART)));
            combat_status_spd.setText(df.format(levelData.getJSONObject(lvlPART).getDouble("speedBase") + levelData.getJSONObject(lvlPART).getDouble("speedAdd") * (seekBar.getProgress() - lvlPART)));
            combat_status_taunt.setText(df.format(levelData.getJSONObject(lvlPART).getDouble("aggro")));
        }
    }
    /*
    jsonObject -> skills[x]
     */
    private void combat_material_init(LinearLayout material_ll, JSONObject jsonObject, int finalX) throws JSONException {
        materialItemsID = new ArrayList<>();
        if (jsonObject.has("levelData")){
            JSONArray levelData = jsonObject.getJSONArray("levelData");
            for (int x = 0 ; x < levelData.length() ; x++){
                JSONArray cost = levelData.getJSONObject(x).getJSONArray("cost");
                for (int y = 0 ; y < cost.length() ; y++){
                    if (y - 1 >= 0){
                        if (!materialItemsID.contains(cost.getJSONObject(y).getInt("id"))){
                            materialItemsID.add(cost.getJSONObject(y).getInt("id"));
                        }
                    }
                }
            }

            int[] acceptPurposeIdDemo = new int[]{ItemRSS.MATERIAL_COMMON_CURRENCY, ItemRSS.MATERIAL_TRACE_MATERIAL_LIGHTCONE_ASCENSION_MATERIALS, ItemRSS.MATERIAL_TRACE_MATERIAL_CHARACTER_ASCENSION_MATERIALS};

            switch (finalX){
                case 0 : acceptPurposeIdDemo =  new int[]{
                        ItemRSS.MATERIAL_COMMON_CURRENCY,
                        ItemRSS.MATERIAL_TRACE_MATERIAL_LIGHTCONE_ASCENSION_MATERIALS,
                        ItemRSS.MATERIAL_TRACE_MATERIAL_CHARACTER_ASCENSION_MATERIALS
                };break;

                case 1 :
                case 2 :
                case 3 :
                    acceptPurposeIdDemo =  new int[]{
                            ItemRSS.MATERIAL_COMMON_CURRENCY,
                            ItemRSS.MATERIAL_TRACE_MATERIAL_LIGHTCONE_ASCENSION_MATERIALS,
                            ItemRSS.MATERIAL_TRACE_MATERIAL_CHARACTER_ASCENSION_MATERIALS,
                            ItemRSS.MATERIAL_TRACE_MATERIALS
                    };break;
                case 4 :
                    acceptPurposeIdDemo =  new int[]{};break;
                case -1 :
                    acceptPurposeIdDemo =  new int[]{
                            ItemRSS.MATERIAL_COMMON_CURRENCY,
                            //ItemRSS.MATERIAL_CHARACTER_EXP_MATERIAL,
                            ItemRSS.MATERIAL_TRACE_MATERIAL_CHARACTER_ASCENSION_MATERIALS,
                            ItemRSS.MATERIAL_CHARACTER_ASCENSION_MATERIALS
                    };break;
            }

            ArrayList<Integer> acceptPurposeId = IntStream.of(acceptPurposeIdDemo).boxed().collect(Collectors.toCollection(ArrayList::new));

            for (int x = 0 ; x < materialItemsRef.size() ; x++){
                if (acceptPurposeId.contains(materialItemsRef.get(x).getPurposeId())){
                    View item_view = LayoutInflater.from(context).inflate(R.layout.item_info_character_material, material_ll, false);
                    ImageView material_ico  = item_view.findViewById(R.id.material_ico);
                    TextView material_tv = item_view.findViewById(R.id.material_tv);
                    item_view.setId(87000000+materialItemsRef.get(x).getId());
                    material_tv.setId(87100000+materialItemsRef.get(x).getId());
                    material_ico.setId(87200000+materialItemsRef.get(x).getId());
                    Picasso.get()
                            .load(item_rss.getMaterialByID(materialItemsRef.get(x).getId()))
                            .fit()
                            .into(material_ico);
                    material_tv.setText("0");
                    material_ico.setBackgroundResource(item_rss.getBgByItemRarity(materialItemsRef.get(x).getRarity()));
                    material_ll.addView(item_view);
                }
            }
        }

    }
    private void combat_material_change(LinearLayout material_ll, JSONObject jsonObject, int max) throws JSONException {
        if (jsonObject.has("levelData")){
            JSONArray levelData = jsonObject.getJSONArray("levelData");
            for (int x = 0 ; x < materialItemsRef.size() ; x++){
                if (material_ll.findViewById(87100000+materialItemsRef.get(x).getId()) != null){
                    TextView material_tv = material_ll.findViewById(87100000+materialItemsRef.get(x).getId());
                    material_tv.setText("0");
                }
            }


            ArrayList<Integer> costSum = new ArrayList<>();
            ArrayList<Integer> costID = new ArrayList<>();
            for (int pos = 0 ; pos < max+1 ; pos++){
                JSONArray cost = levelData.getJSONObject(pos).getJSONArray("cost");
                for (int x = 0 ; x < cost.length(); x++) {
                    if (costID.contains(cost.getJSONObject(x).getInt("id"))){
                        int position = costID.indexOf(cost.getJSONObject(x).getInt("id"));
                        costSum.set(position, costSum.get(position) + cost.getJSONObject(x).getInt("count"));
                    }else{
                        costID.add(cost.getJSONObject(x).getInt("id"));
                        costSum.add(cost.getJSONObject(x).getInt("count"));
                    }
                }
            }

            if (jsonObject.has("calculator")){

            }

            for (int x = 0 ; x < costID.size(); x++){
                if (material_ll.findViewById(87100000+costID.get(x)) != null){
                    TextView material_tv = material_ll.findViewById(87100000+costID.get(x));
                    material_tv.setText(String.valueOf(costSum.get(x)));
                }
            }


        }
    }

    /*
    jsonObject -> skills[x]
     */
    private void combat_desc_change(TextView descTV, JSONObject jsonObject, SeekBar seekBar) throws JSONException {
        if (jsonObject.has("levelData")){
            JSONArray levelData = jsonObject.getJSONArray("levelData");
            String desc = jsonObject.getString("descHash");
            if (levelData.length() > seekBar.getProgress()){
                JSONArray params = levelData.getJSONObject(seekBar.getProgress()).getJSONArray("params");
                descTV.setText(ItemRSS.valuedText(desc, params, context), TextView.BufferType.SPANNABLE);
            }
            seekBar.setMax(levelData.length()-1);
        }
    }

    private void init_eidolon(JSONObject jsonObject) throws JSONException {
        JSONArray ranks = jsonObject.getJSONArray("ranks");

        ImageView eidolon_ico1 = info_eidolon.findViewById(R.id.eidolon_ico1);
        ImageView eidolon_ico2 = info_eidolon.findViewById(R.id.eidolon_ico2);
        ImageView eidolon_ico3 = info_eidolon.findViewById(R.id.eidolon_ico3);
        ImageView eidolon_ico4 = info_eidolon.findViewById(R.id.eidolon_ico4);
        ImageView eidolon_ico5 = info_eidolon.findViewById(R.id.eidolon_ico5);
        ImageView eidolon_ico6 = info_eidolon.findViewById(R.id.eidolon_ico6);
        TextView eidolon_name1 = info_eidolon.findViewById(R.id.eidolon_name1);
        TextView eidolon_name2 = info_eidolon.findViewById(R.id.eidolon_name2);
        TextView eidolon_name3 = info_eidolon.findViewById(R.id.eidolon_name3);
        TextView eidolon_name4 = info_eidolon.findViewById(R.id.eidolon_name4);
        TextView eidolon_name5 = info_eidolon.findViewById(R.id.eidolon_name5);
        TextView eidolon_name6 = info_eidolon.findViewById(R.id.eidolon_name6);
        TextView eidolon_desc1 = info_eidolon.findViewById(R.id.eidolon_desc1);
        TextView eidolon_desc2 = info_eidolon.findViewById(R.id.eidolon_desc2);
        TextView eidolon_desc3 = info_eidolon.findViewById(R.id.eidolon_desc3);
        TextView eidolon_desc4 = info_eidolon.findViewById(R.id.eidolon_desc4);
        TextView eidolon_desc5 = info_eidolon.findViewById(R.id.eidolon_desc5);
        TextView eidolon_desc6 = info_eidolon.findViewById(R.id.eidolon_desc6);

        eidolon_ico1.setImageResource(item_rss.getCharByName(hsrItem.getName())[3]);
        eidolon_ico2.setImageResource(item_rss.getCharByName(hsrItem.getName())[4]);
        eidolon_ico3.setImageResource(item_rss.getCharByName(hsrItem.getName())[5]);
        eidolon_ico4.setImageResource(item_rss.getCharByName(hsrItem.getName())[6]);
        eidolon_ico5.setImageResource(item_rss.getCharByName(hsrItem.getName())[7]);
        eidolon_ico6.setImageResource(item_rss.getCharByName(hsrItem.getName())[8]);

        eidolon_name1.setText(ranks.getJSONObject(0).getString("name"));
        eidolon_name2.setText(ranks.getJSONObject(1).getString("name"));
        eidolon_name3.setText(ranks.getJSONObject(2).getString("name"));
        eidolon_name4.setText(ranks.getJSONObject(3).getString("name"));
        eidolon_name5.setText(ranks.getJSONObject(4).getString("name"));
        eidolon_name6.setText(ranks.getJSONObject(5).getString("name"));

        eidolon_desc1.setText(ItemRSS.valuedText(ranks.getJSONObject(0).getString("descHash"), ranks.getJSONObject(0).getJSONArray("params"),context), TextView.BufferType.SPANNABLE);
        eidolon_desc2.setText(ItemRSS.valuedText(ranks.getJSONObject(1).getString("descHash"), ranks.getJSONObject(1).getJSONArray("params"),context), TextView.BufferType.SPANNABLE);
        eidolon_desc3.setText(ItemRSS.valuedText(ranks.getJSONObject(2).getString("descHash"), ranks.getJSONObject(2).getJSONArray("params"),context), TextView.BufferType.SPANNABLE);
        eidolon_desc4.setText(ItemRSS.valuedText(ranks.getJSONObject(3).getString("descHash"), ranks.getJSONObject(3).getJSONArray("params"),context), TextView.BufferType.SPANNABLE);
        eidolon_desc5.setText(ItemRSS.valuedText(ranks.getJSONObject(4).getString("descHash"), ranks.getJSONObject(4).getJSONArray("params"),context), TextView.BufferType.SPANNABLE);
        eidolon_desc6.setText(ItemRSS.valuedText(ranks.getJSONObject(5).getString("descHash"), ranks.getJSONObject(5).getJSONArray("params"),context), TextView.BufferType.SPANNABLE);

        LinearLayout eidolon_ll_1 = info_eidolon.findViewById(R.id.eidolon_ll_1);
        LinearLayout eidolon_ll_2 = info_eidolon.findViewById(R.id.eidolon_ll_2);
        LinearLayout eidolon_ll_3 = info_eidolon.findViewById(R.id.eidolon_ll_3);
        LinearLayout eidolon_ll_4 = info_eidolon.findViewById(R.id.eidolon_ll_4);
        LinearLayout eidolon_ll_5 = info_eidolon.findViewById(R.id.eidolon_ll_5);
        LinearLayout eidolon_ll_6 = info_eidolon.findViewById(R.id.eidolon_ll_6);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }


    public void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    private void lightcone_list_reload() {
        lightconesList = new ArrayList<>();
        String name ,path,status,fileName;
        int rare;
        //charactersList.clear();

        String json_base = LoadAssestData(context,"lightcone_data/lightcone_list.json");;
        //Get data from JSON
        try {
            JSONArray array = new JSONArray(json_base);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                name = object.getString("name");
                path = object.getString("path");
                rare = object.getInt("rare");
                status = object.getString("status");
                fileName = object.getString("fileName");

                HSRItem hsrItem = new HSRItem();
                hsrItem.setName(name);
                hsrItem.setPath(path);
                hsrItem.setRare(rare);
                hsrItem.setStatus(status);
                hsrItem.setFileName(fileName);

                lightconesList.add(hsrItem);
                lightconesNameList.add(name);
            }
        } catch (JSONException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogExport.bugLog(TAG, "lightcone_list_reload", sw.toString(), context);
        }
    }
}