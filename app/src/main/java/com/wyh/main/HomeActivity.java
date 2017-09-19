package com.wyh.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wyh.slide.FooterBind;
import com.wyh.slide.HeaderBind;
import com.wyh.slide.ItemType;
import com.wyh.slide.BottomListener;
import com.wyh.slide.SlideAdapter;
import com.xmwj.slidingmenu.R;
import com.wyh.slide.ItemBind;
import com.wyh.slide.ItemView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static void ddd(String text) {
        Log.d("YHAO", text);
    }

    public static void eee(String text) {
        Log.e("YHAO", text);
    }


    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        init();
    }

    private void init() {


        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("我是第" + i + "个item");
        }

        final List<String> data2 = new ArrayList<>();
        data2.add("我是一个新item");


        ItemBind<String> itemBind = new ItemBind<String>() {
            @Override
            public void onBind(final ItemView itemView, String data, int position) {
                itemView.setText(R.id.textView, data)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(HomeActivity.this, "click", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnClickListener(R.id.textView, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(HomeActivity.this, "textView click", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setOnClickListener(R.id.icon, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(HomeActivity.this, "menu click", Toast.LENGTH_SHORT).show();
                                itemView.closeMenu();
                            }
                        });
            }
        };

        //todo 兼容 grid
        //todo bug：菜单没有及时关闭


        SlideAdapter.load(data)
                .item(R.layout.item, 0, 0, R.layout.menu, 0.1f)
                .item(R.layout.item2)
                .divider(1, R.color.colorPrimary)
                .header(R.layout.head, 0.1f)
                .header(R.layout.head, 0.1f)
                .footer(R.layout.foot, 0.1f)
                .type(new ItemType<String>() {

                    @Override
                    public int getItemOrder(String data, int position) {
                        return position % 2 + 1;
                    }
                })
                .bind(itemBind)
                .bind(new HeaderBind() {
                    @Override
                    public void onBind(ItemView header, int order) {
                        if (order == 1) {
                            header.setText(R.id.headText, "第一个 头部");
                        }
                        header.setOnClickListener(R.id.headText, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(HomeActivity.this, "head click", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .bind(new FooterBind() {
                    @Override
                    public void onBind(ItemView footer, int order) {
                        footer.setOnClickListener(R.id.headText, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(HomeActivity.this, "foot click", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .listen(new BottomListener() {

                    @Override
                    public void onBottom(final ItemView footer, final SlideAdapter slideAdapter) {
                        ddd("bottom");
                        footer.setText(R.id.footerText, "正在加载，请稍后...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        slideAdapter.loadMore(data2);
                                        footer.setText(R.id.footerText, "正在加载");
                                    }
                                });

                            }
                        }).start();
                    }
                })
                .into(mRecyclerView);


    }
}
