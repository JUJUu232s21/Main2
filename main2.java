import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import redis.clients.jedis.Jedis;
public class MainActivity2 extends AppCompatActivity {
    private PermissionHelper permissionHelper;
    private final ActivityResultLauncher<Intent> activityCLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {onReturnedFromActivityC();});
    private Context context;
    private Redis_TLS_CONNECT redisTls = new Redis_TLS_CONNECT();
    private SSLSocketFactory sslSocketFactory;
    private HostnameVerifier hostnameVerifier = (hostname, session) -> true;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private static String REDIS_HOST = "";
    private static final int REDIS_PORT = 6381;
    private static String REDIS_PASSWORD = "";
    private static String USER_NAME = "";
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;
    private List<TextModelRecycler> data = new ArrayList<>();
    private HorizontalAdapter adapter;
    private TextView Title_text_top;
    private ListView photo_list;
    private ListView manylist;
    private List<Object> photos = new ArrayList<>();
    private PhotoAdapter adapter2 = new PhotoAdapter(this, photos);
    private FrameLayout frame_layout;
    private com.google.android.material.imageview.ShapeableImageView icon_proff;
    private com.google.android.material.imageview.ShapeableImageView reload_media_btn;
    private byte[] imageBlob;
    private Long photo_id;
    private LinearLayout add_liner_photo;
    private com.google.android.material.imageview.ShapeableImageView new_photo_seelct;
    private List<SearcMain4.SearchModel> searchList = new ArrayList<>();
    private SearcMain4 adapter1;
    private List<String> elements;
    private EditText search_tag_edit;
    private LinearLayout selecttaglayout;
    private TextView select_text_tags;
    private TextView title_text_top_name;
    private Set<Integer> userNumbers = new HashSet<>();
    private Random random = new Random();
    private int intphotominfor = 10;
    private boolean statysloaded = false;
    private LinearLayout liner_3btn;
    private LinearLayout add_first_you_photo;
    private ListView visible_list_and_loads;
    private int StartTagInt = 0;
    private int EndIntTag = 20;
    private boolean statysloadlist = false;
    private int StartRecycler = 0;
    private int EndRecycler = 20;
    private boolean statycRecycler = false;
    private boolean statysselectload_ = true;
    private String selectRecyclertxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(add_liner_photo.getVisibility() == View.VISIBLE){
                    close2_xtag();
                } else {
                    if (add_first_you_photo.getVisibility() == View.VISIBLE) {
                        close2_t();
                    } else {
                        finishAffinity();
                        overridePendingTransition(0, 0);
                    }
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        context = this;
        try {
            sslSocketFactory = redisTls.getCustomSSLSocketFactory(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = getSharedPreferences("Setting_Register", Context.MODE_PRIVATE);
        String savedText = sharedPref.getString("You_Register", "");
        USER_NAME = savedText;
        REDIS_HOST = Redis_Auth.REDIS_HOST;
        REDIS_PASSWORD = Redis_Auth.REDIS_PASSWORD;

        recyclerView = findViewById(R.id.recyclerView);
        photo_list = findViewById(R.id.photo_list);
        Title_text_top = findViewById(R.id.Title_text_top);
        frame_layout = findViewById(R.id.frame_layout);
        icon_proff = findViewById(R.id.set_icon);
        permissionHelper = new PermissionHelper(this);
        add_liner_photo = findViewById(R.id.add_liner_photo);
        manylist = findViewById(R.id.manylist);
        new_photo_seelct = findViewById(R.id.new_photo_seelct);
        search_tag_edit = findViewById(R.id.search_tag_edit);
        selecttaglayout = findViewById(R.id.selecttaglayout);
        select_text_tags = findViewById(R.id.select_text_tags);
        title_text_top_name = findViewById(R.id.title_text_top_name);
        reload_media_btn = findViewById(R.id.reload_media_btn);
        liner_3btn = findViewById(R.id.liner_3btn);
        add_first_you_photo = findViewById(R.id.add_first_you_photo);
        visible_list_and_loads = findViewById(R.id.visible_list_and_loads);

        Title_text_top.post(() -> {
            TextPaint paint = Title_text_top.getPaint();
            float width = paint.measureText(Title_text_top.getText().toString());
            Shader textShader = new LinearGradient(0f, 0f, width, 0f, new int[]{
                    Color.parseColor("#22EDFF"),
                    Color.parseColor("#5061BA"),
                    Color.parseColor("#0A27D1")
            }, new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP);
            paint.setShader(textShader);
            Title_text_top.invalidate();
        });

        photo_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstChild = view.getChildAt(0);
                    if (firstChild != null) {
                        int topOffset = firstChild.getTop() - view.getPaddingTop();
                        if (topOffset < 0) {
                            frame_layout.setVisibility(View.GONE);
                            reload_media_btn.setVisibility(View.GONE);
                        } else {
                            frame_layout.setVisibility(View.VISIBLE);
                            reload_media_btn.setVisibility(View.VISIBLE);
                        }
                    }
                }
                    if (totalItemCount > 0 && (firstVisibleItem + visibleItemCount) >= totalItemCount) {
                        View lastChild = view.getChildAt(view.getChildCount() - 1);
                        if (lastChild != null) {
                            int bottomOffset = lastChild.getBottom() - (view.getHeight() - view.getPaddingBottom());
                            if (bottomOffset <= 0) {
                                if(statysselectload_){
                                    if (statysloaded){
                                        statysloaded = false;
                                        Load_Any_Image_To_list();
                                    }
                                } else {
                                    if (loadaddselect) {
                                        Log.d("MY_LOGS", "СБРАСЫВАЕТСЯ В SCROLL! Сдвиг с " + NameStartPhoto + " до " + (NameStartPhoto + 5));
                                        loadaddselect = false;
                                        NameStartPhoto = NameStartPhoto + 5;
                                        NameEndPHOTO = NameEndPHOTO + 5;
                                        Load_SelectRedisPhoro(selectRecyclertxt);
                                    }
                                }
                            }
                        }
                    }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HorizontalAdapter(data, (item, position) -> {
            String current = item.getText();
            if (!current.equals("+") && !current.equals("Все")) {
                templist_vse.clear();
                userNumbers.clear();
                photos.clear();
                statysselectload_ = false;
                statysloaded = false;
                loadaddselect = false;
                NameStartPhoto = 0;
                NameEndPHOTO = 10;
                selectRecyclertxt = current;
                Log.d("MY_LOGS", "1. КЛИК ПО ТЕГУ: " + current + " | NameStartPhoto=" + NameStartPhoto + " | loadaddselect=" + loadaddselect);
                Load_SelectRedisPhoro(selectRecyclertxt);
            }
            if(current.equals("Все")) {
                userNumbers.clear();
                photos.clear();
                templist_vse.clear();
                statysselectload_ = true;
                statysloaded = false;
                loadaddselect = false;
                Load_Any_Image_To_list();
            }
            if (current.equals("+")) {
                StartRecycler = 0;
                EndRecycler = 20;
                statycRecycler = false;
                searchList.clear();
                adapter1.notifyDataSetChanged();
                setadd_new_photosears();
            }}, (item, position) -> {
            String current = item.getText();
            AlertDialog dialog = new AlertDialog.Builder(MainActivity2.this, R.style.CustomAlertDialog)
                    .setTitle(current)
                    .setMessage("Удалить этот тег из быстрого поиска?")
                    .setCancelable(false)
                    .setPositiveButton("Удалить", (d, whitch)-> {
                        new Thread(()->{
                            try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                                jedis.auth(REDIS_PASSWORD);
                                jedis.lrem(USER_NAME + "_Настройки ленты", 0 , current);
                                mainHandler.post(()->{
                                    data.clear();
                                    adapter.notifyDataSetChanged();
                                    Load_List_Top();
                                });
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }).start();
                    })
                    .setNegativeButton("Отмена", null)
                    .create();
            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
            });
            dialog.show();
            return true;
        });

        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            photo_list.setPadding(photo_list.getPaddingLeft(), photo_list.getPaddingTop(), photo_list.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        Title_text_top.setText("Соединение...");
        adapter1 = new SearcMain4(this, searchList, null);
        manylist.setAdapter(adapter1);
        visible_list_and_loads.setAdapter(adapter1);

        search_tag_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                adapter1.getFilter().filter(text);
            }
        });

        manylist.setOnItemClickListener((parent, view, position, id) -> {
            SearcMain4.SearchModel selectedItem = (SearcMain4.SearchModel) parent.getItemAtPosition(position);
            if (selectedItem != null) {
                String tagName = selectedItem.getName();
                if(!select_text_tags.getText().toString().equals("Выбран тег: " + tagName)){
                    select_text_tags.setText("Выбран тег: " + tagName);
                    selecttaglayout.setVisibility(View.VISIBLE);
                }
            }
        });

        manylist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (lastVisibleItem == totalItemCount) {
                        View lastView = view.getChildAt(visibleItemCount - 1);
                        if (lastView != null && lastView.getBottom() <= view.getHeight()) {
                            if (statysloadlist){
                                statysloadlist = false;
                                StartTagInt = StartTagInt + 5;
                                EndIntTag = EndIntTag + 5;
                                dowloadTags();
                            }
                        }
                    }
                }
            }
        });

        visible_list_and_loads.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if(lastVisibleItem == totalItemCount){
                        View lastView = view.getChildAt(visibleItemCount - 1);
                        if (lastView != null && lastView.getBottom() <= view.getHeight()) {
                            if(statycRecycler){
                                statycRecycler = false;
                                StartRecycler = StartRecycler + 5;
                                EndRecycler = EndRecycler + 5;
                                setadd_new_photosears();
                            }
                        }
                    }
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        visible_list_and_loads.setOnItemClickListener((parent, view, position, id) -> {
            SearcMain4.SearchModel selectedItem = (SearcMain4.SearchModel) parent.getItemAtPosition(position);
            if(selectedItem != null){
                String tagName = selectedItem.getName();
                new Thread(()->{
                    try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                        jedis.auth(REDIS_PASSWORD);
                        Long index = jedis.lpos(USER_NAME + "_Настройки ленты", tagName);
                        if(index == null){
                            jedis.rpush(USER_NAME + "_Настройки ленты", tagName);
                        }
                        mainHandler.post(()->{
                            adapter1.notifyDataSetChanged();
                            add_first_you_photo.setVisibility(View.GONE);
                            SharedPreferences sharedPref4 = getSharedPreferences("User_Top_list", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor4 = sharedPref4.edit();
                            editor4.putString("list_top", "");
                            editor4.apply();
                            data.clear();
                            Load_List_Top();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        adapter2.setOnPhotoClickListener((position, photo) -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
            intent.putExtra("Key_Name_Phto", templist_vse.get(position));
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        Load_Icons();
        Load_Image_Any();
        Load_You_Likes_Tag();
    }

    private void Load_You_Likes_Tag(){
        SharedPreferences sharedPref1 = getSharedPreferences("User_Top_list", Context.MODE_PRIVATE);
        String savedText1 = sharedPref1.getString("list_top", "");
        if(savedText1.equals("")){
            data.clear();
            Load_List_Top();
        } else {
            if(savedText1.equals("1")){
                data.add(new TextModelRecycler("Все"));
                data.add(new TextModelRecycler("+"));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void Load_Image_Any(){
        new Thread(()->{
            try {
                try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                    jedis.auth(REDIS_PASSWORD);
                    String val = jedis.get("ВСЕГО_ЗАГРУЖНО_ФОТО");
                    if (val != null && !val.equals("0")) {
                        photo_id = Long.parseLong(val);
                        mainHandler.post(()-> {
                            Load_Any_Image_To_list();
                        });
                    } else {
                        statysloaded = false;
                    }
                    mainHandler.post(()->{
                        Title_text_top.setText(R.string.app_name);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    mainHandler.post(()->{
                        Title_text_top.setText("Нет Соединения...");
                    });
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    private List<String> templist_vse = new ArrayList<>();
    private void Load_Any_Image_To_list() {
        if (userNumbers.size() >= photo_id) {
            // Все фотографии уже загружены
            return;
        }
        new Thread(()->{
            try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)) {
                jedis.auth(REDIS_PASSWORD);
                for (int a = 0; a < intphotominfor; a++) {
                    if (!statysselectload_) {
                        break;
                    }
                    int numb;
                    do {
                        numb = getRandomNumber(random, 1, photo_id);
                    } while (!userNumbers.add(numb));
                    byte[] imageBlob = jedis.hget("Фотографии".getBytes(StandardCharsets.UTF_8), ("Фото под номером: " + numb).getBytes(StandardCharsets.UTF_8));
                    if (imageBlob != null) {
                        Bitmap bitmap = Class_Uri_To_Blob.blobToBitmap(imageBlob);
                        if (bitmap != null) {
                            int finalNumb = numb;
                            mainHandler.post(() -> {
                                if(statysselectload_){
                                    if(!templist_vse.contains("Фото под номером: " + finalNumb)){
                                        templist_vse.add("Фото под номером: " + finalNumb);
                                        photos.add(bitmap);
                                        if (photo_list.getAdapter() == null) {
                                            photo_list.setAdapter(adapter2);
                                        }
                                        adapter2.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }
                mainHandler.post(()->{
                    statysloaded = true;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int NameStartPhoto = 0;
    private int NameEndPHOTO = 10;
    private boolean loadaddselect = false;
    private void Load_SelectRedisPhoro(String name){
        // <<< ЛОГ ВХОДА В МЕТОД >>>
        Log.d("MY_LOGS", "2. ВХОД В Load_SelectRedisPhoro: name=" + name + " | NameStartPhoto=" + NameStartPhoto + " | NameEndPHOTO=" + NameEndPHOTO);
        new Thread(()->{
            try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)) {
                jedis.auth(REDIS_PASSWORD);
                List<String> photoname = jedis.lrange(name, NameStartPhoto, NameEndPHOTO);
                Log.d("MY_LOGS", "3. ОТВЕТ ИЗ REDIS (" + name + "): получено элементов=" + (photoname != null ? photoname.size() : "null"));
                if(photoname.isEmpty()){
                    Log.d("MY_LOGS", "   --> REDIS ВЕРНУЛ ПУСТОЙ СПИСОК!");
                    return;
                }
                Log.d("LOOOD", String.valueOf(2));
                for (String tag : photoname) {
                    if(!templist_vse.contains(tag)){
                        byte[] imageBlob = jedis.hget("Фотографии".getBytes(StandardCharsets.UTF_8), (tag).getBytes(StandardCharsets.UTF_8));
                        if (imageBlob != null) {
                            Bitmap bitmap = Class_Uri_To_Blob.blobToBitmap(imageBlob);
                            if (bitmap != null) {
                                mainHandler.post(() -> {
                                    photos.add(bitmap);
                                    templist_vse.add(tag);
                                    if (photo_list.getAdapter() == null) {
                                        photo_list.setAdapter(adapter2);
                                    }
                                    adapter2.notifyDataSetChanged();
                                });
                            }
                        }
                    }
                }
                Log.d("MY_LOGS", "4. УСПЕШНО ЗАГРУЖЕНО, loadaddselect=true");
                loadaddselect = true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    private void Load_Icons(){
        SharedPreferences sharedPref = getSharedPreferences("Icon_Profiles", Context.MODE_PRIVATE);
        String cachedIcon = sharedPref.getString("You_Icon" + USER_NAME, "");
        if (!cachedIcon.isEmpty()) {
            byte[] cachedBlob = Class_Uri_To_Blob.base64ToBlob(cachedIcon);
            Bitmap cachedBitmap = Class_Uri_To_Blob.blobToBitmap(cachedBlob);
            if (cachedBitmap != null) {
                icon_proff.setImageBitmap(cachedBitmap);
                return;
            }
        }
        executor.execute(()->{
            try {
                try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                    jedis.auth(REDIS_PASSWORD);
                    imageBlob = jedis.hget("Иконки_пользователй".getBytes(), (USER_NAME + "_Фото_Иконки").getBytes(StandardCharsets.UTF_8));
                    if (imageBlob != null) {
                        Bitmap bitmap = Class_Uri_To_Blob.blobToBitmap(imageBlob);
                        String base64Icon = Class_Uri_To_Blob.blobToBase64(imageBlob);
                        mainHandler.post(() -> {
                            if (bitmap != null) {
                                icon_proff.setImageBitmap(bitmap);
                            }
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("You_Icon" + USER_NAME, base64Icon);
                            editor.apply();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSystemBars();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void Load_List_Top(){
        new Thread(()->{
                try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                    jedis.auth(REDIS_PASSWORD);
                    List<String> listredis = jedis.lrange(USER_NAME + "_Настройки ленты", 0, -1);
                    if (listredis == null || listredis.isEmpty()) {
                        mainHandler.post(()->{
                            SharedPreferences sharedPref = getSharedPreferences("User_Top_list", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("list_top", "1");
                            editor.apply();
                            data.add(new TextModelRecycler("Все"));
                            data.add(new TextModelRecycler("+"));
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        mainHandler.post(()->{
                            data.add(new TextModelRecycler("Все"));
                            for(String tag : listredis){
                                data.add(new TextModelRecycler(tag));
                            }
                            data.add(new TextModelRecycler("+"));
                            adapter.notifyDataSetChanged();
                        });
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                updateSystemBars();
            }
        });
    }

    private void updateSystemBars() {
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.setAppearanceLightStatusBars(false);
            controller.setAppearanceLightNavigationBars(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) liner_3btn.getLayoutParams();
        int marginInDp = isInMultiWindowMode() ? 20 : 50;
        params.bottomMargin = (int) (marginInDp * getResources().getDisplayMetrics().density);
        liner_3btn.setLayoutParams(params);
    }

    public void user_profiles(View view){
        Intent intent = new Intent(this, MainActivity3.class);
        activityCLauncher.launch(intent);
        overridePendingTransition(0, 0);
    }

    public void search_panel(View view){
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void onReturnedFromActivityC() {
        Load_Icons();
    }

    public void close2(View view){
        close2_xtag();
    }

    private void close2_xtag(){
        CloseKeyBoard.hideKeyboard(MainActivity2.this);
        add_liner_photo.setVisibility(View.GONE);
        search_tag_edit.setText("");
        searchList.clear();
        elements.clear();
        select_text_tags.setText("");
        adapter1.notifyDataSetChanged();
        selecttaglayout.setVisibility(View.GONE);
        title_text_top_name.setText("");
    }

    public void addnewphoto(View view){
        CheckedPermissionPhoto();
    }
    public void delete_and_new(View view){
        CheckedPermissionPhoto();
    }

    private void open_files(){
        imagePickerLauncher.launch("image/*");
    }

    ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            add_liner_photo.setVisibility(View.VISIBLE);
            imageBlob = Class_Uri_To_Blob.uriToBlob(MainActivity2.this, uri);
            new_photo_seelct.setImageURI(uri);
            search_tag_edit.setText("");
            searchList.clear();
            statysloadlist = false;
            StartTagInt = 0;
            EndIntTag = 20;
            dowloadTags();
        }
    });

    private void dowloadTags(){
        new Thread(()->{
            try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                jedis.auth(REDIS_PASSWORD);
                elements = jedis.lrange("Все общие теги", StartTagInt, EndIntTag);
                mainHandler.post(()-> {
                    if(elements != null && !elements.isEmpty()){
                        for(String tag : elements){
                            boolean exists = searchList.stream().anyMatch(model -> model.getName().equals(tag));
                            if (!exists) {
                                searchList.add(new SearcMain4.SearchModel(tag, 0));
                            }
                        }
                        adapter1.notifyDataSetChanged();
                        statysloadlist = true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void CheckedPermissionPhoto(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            // Android 13 (API 33) и выше
            permissionHelper.check(Manifest.permission.READ_MEDIA_IMAGES, status -> {
                if (status == 1) {
                    open_files();
                } else {
                    Toast.makeText(this, "Нет доступа к фотографиям!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Android 12 (API 32) и ниже
            permissionHelper.check(Manifest.permission.READ_EXTERNAL_STORAGE, status -> {
                if (status == 1) {
                    open_files();
                } else {
                    Toast.makeText(this, "Нет доступа к фотографиям!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void addnewsearchtag(View view) {
        title_text_top_name.clearFocus();
        search_tag_edit.clearFocus();
        search_tag_edit.setText("");
        EditText input = new EditText(MainActivity2.this);
        input.setHint("Создайте тег...");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        input.setTypeface(null, Typeface.BOLD);
        input.setTextColor(Color.BLUE);
        input.setHintTextColor(Color.WHITE);
        int paddingInDp = 10;
        int paddingInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingInDp, getResources().getDisplayMetrics());
        input.setPadding(paddingInPx, input.getPaddingTop(), paddingInPx, input.getPaddingBottom());

        AlertDialog dialog = new AlertDialog.Builder(MainActivity2.this, R.style.CustomAlertDialog)
                .setTitle("Новый тег")
                .setMessage("Добавить новый поисковый тег?")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("Добавить", null)
                .setNegativeButton("Отмена", (d, which) -> {
                    CloseKeyBoard.hideKeyboard(this);
                })
                .create();
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.blue));
            negativeButton.setTextColor(getResources().getColor(R.color.blue));
            positiveButton.setOnClickListener(v -> {
                String tagText = input.getText().toString().trim();
                if (tagText.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "Введите тег!", Toast.LENGTH_SHORT).show();
                    input.setSelection(0);
                } else {
                    new Thread(()->{
                        try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                            jedis.auth(REDIS_PASSWORD);
                            Long listcheck = jedis.lpos("Все общие теги", tagText);
                            if(listcheck != null){
                                mainHandler.post(()->{
                                    Toast.makeText(MainActivity2.this, "Тег уже существует!", Toast.LENGTH_SHORT).show();
                                    input.setText("");
                                });
                            } else {
                                jedis.rpush("Все общие теги", tagText);
                                mainHandler.post(()->{
                                    searchList.clear();
                                    adapter1.notifyDataSetChanged();
                                    statysloadlist = false;
                                    StartTagInt = 0;
                                    EndIntTag = 20;
                                    dowloadTags();
                                    selecttaglayout.setVisibility(View.VISIBLE);
                                    select_text_tags.setText("Выбран тег: " + tagText);
                                    dialog.dismiss();
                                    });
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }).start();
                }
            });
        });
        dialog.show();
    }

    public void download_photosredis(View view){
        if(selecttaglayout.getVisibility() == View.GONE){
            Toast.makeText(this, "Выберите тег!", Toast.LENGTH_SHORT).show();
        } else {
            if(title_text_top_name.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Введите название!", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(()-> {
                    try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                        jedis.auth(REDIS_PASSWORD);
                        String val = jedis.get("ВСЕГО_ЗАГРУЖНО_ФОТО");
                        int num = Integer.parseInt(val) + 1;
                        jedis.set("ВСЕГО_ЗАГРУЖНО_ФОТО", String.valueOf(num));
                        jedis.hset("Фотографии".getBytes(), ("Фото под номером: " + num).getBytes(StandardCharsets.UTF_8), imageBlob);
                        jedis.hset("Названия фотографий", "Фото под номером: " + num, title_text_top_name.getText().toString().trim());
                        jedis.lpush(select_text_tags.getText().toString().replace("Выбран тег: ", ""),"Фото под номером: " + num);
                        jedis.lpush(USER_NAME + "_Загрузил фотографии", "Фото под номером: " + num);
                        jedis.hset("Кто загрузил фото", "Фото под номером: " + num, USER_NAME);
                        jedis.hset("ТЕГИ И ФОТО", "Фото под номером: " + num, select_text_tags.getText().toString().replace("Выбран тег: ", ""));
                        mainHandler.post(()->{
                            CloseKeyBoard.hideKeyboard(MainActivity2.this);
                            add_liner_photo.setVisibility(View.GONE);
                            search_tag_edit.setText("");
                            title_text_top_name.setText("");
                            searchList.clear();
                            select_text_tags.setText("");
                            adapter1.notifyDataSetChanged();
                            selecttaglayout.setVisibility(View.GONE);
                            loaddobble();
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    public static int getRandomNumber(Random random, int min, Long max) {
        if (max == null || max < min) {
            return min;
        }
        int bound = (int) (max - min + 1);
        if (bound <= 0) {
            return min;
        }
        return random.nextInt(bound) + min;
    }

    private void loaddobble(){
        new Thread(()->{
            try {
                try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                    jedis.auth(REDIS_PASSWORD);
                    String val = jedis.get("ВСЕГО_ЗАГРУЖНО_ФОТО");
                    if (val != null && !val.equals("0")) {
                        photo_id = Long.parseLong(val);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public void Start_listview(View view){
        photo_list.setSelection(0);
    }

    public void reloads_photo_list(View view){
        if(statysselectload_){
            statysloaded = false;
            userNumbers.clear();
            photos.clear();
            templist_vse.clear();
            adapter2.notifyDataSetChanged();
            Load_Any_Image_To_list();
        } else {
            templist_vse.clear();
            statysselectload_ = false;
            statysloaded = false;
            userNumbers.clear();
            photos.clear();
            adapter2.notifyDataSetChanged();
            NameStartPhoto = 0;
            NameEndPHOTO = 10;
            Load_SelectRedisPhoro(selectRecyclertxt);
        }
    }

    private void setadd_new_photosears(){
        add_first_you_photo.setVisibility(View.VISIBLE);
        new Thread(()->{
            try(Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, 9999, true, sslSocketFactory, null, hostnameVerifier)){
                jedis.auth(REDIS_PASSWORD);
                elements = jedis.lrange("Все общие теги", StartRecycler, EndRecycler);
                mainHandler.post(()->{
                    if(elements != null && !elements.isEmpty()){
                        for(String tag : elements){
                            boolean exists = searchList.stream().anyMatch(model -> model.getName().equals(tag));
                            if (!exists) {
                                searchList.add(new SearcMain4.SearchModel(tag, 0));
                            }
                        }
                        adapter1.notifyDataSetChanged();
                        statycRecycler = true;
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public void close2_tag(View view){
        close2_t();
    }
    private void close2_t(){
        elements.clear();
        searchList.clear();
        adapter1.notifyDataSetChanged();
        add_first_you_photo.setVisibility(View.GONE);
    }
}