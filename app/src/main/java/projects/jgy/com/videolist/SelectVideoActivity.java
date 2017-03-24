package projects.jgy.com.videolist;

import android.app.Dialog;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import projects.jgy.com.videolist.adapter.SelectVideoActivity_BottomListDialogAdapter;
import projects.jgy.com.videolist.base.DefaultBaseActivity;
import projects.jgy.com.videolist.bean.Video;
import projects.jgy.com.videolist.dialog.BottomListDialog;
import projects.jgy.com.videolist.inteface.AbstructProvider;
import projects.jgy.com.videolist.provider.VideoProvider;
import projects.jgy.com.videolist.utils.AdapterUtils;
import projects.jgy.com.videolist.utils.StatusBarHeightUtil;

public class SelectVideoActivity extends DefaultBaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Map<String, List<Video>> AllList;
    private RelativeLayout actionbar;
    private ImageView img_album_arrow;
    private TextView select_video;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_select_video);
    }

    @Override
    protected void initView() {
        actionbar = (RelativeLayout) findViewById(R.id.actionbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.Gray, R.color.Gray, R.color.Gray, R.color.Gray);
        startRefreshing(swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        findViewById(R.id.title_back).setOnClickListener(this);
        findViewById(R.id.select_video).setOnClickListener(this);

        img_album_arrow = (ImageView) findViewById(R.id.img_album_arrow);
        select_video = (TextView) findViewById(R.id.select_video);
    }

    @Override
    protected void initData() {
        super.initData();
        new initVideosThread().start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.select_video:
                if (bottomListDialog != null) {
                    bottomListDialog.show();
                    img_album_arrow.setSelected(true);
                }
                break;

        }
    }

    private BottomListDialog bottomListDialog;
    private Adapter adapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AppConstant.WHAT.SUCCESS:
                    stopRefreshing(swipeRefreshLayout);
                    adapter = new Adapter(R.layout.adapter_select_video_item, (List<Video>) msg.obj);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                        @Override
                        public void SimpleOnItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                            Toast.makeText(context, "video_path====" + ((List<Video>) adapter.getData()).get(position).getPath(), Toast.LENGTH_LONG);
                        }
                    });

                    final SelectVideoActivity_BottomListDialogAdapter bottomListDialogAdapter = new SelectVideoActivity_BottomListDialogAdapter(activity, AllList);

                    bottomListDialog = new BottomListDialog.Builder(activity
                            , bottomListDialogAdapter,
                            MyApplication.getInstance().getScreenHeight() - actionbar.getHeight() - StatusBarHeightUtil.getStatusBarHeight(context)
                    ).setOnItemClickListener(new BottomListDialog.OnItemClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            String album = (String) bottomListDialogAdapter.getAllList().keySet().toArray()[which];
                            adapter.setNewData(bottomListDialogAdapter.getAllList().get(album));
                            select_video.setText(album);
                            img_album_arrow.setSelected(false);
                        }
                    }).create();
                    break;

                case AppConstant.WHAT.FAILURE:
                    stopRefreshing(swipeRefreshLayout);
                    break;
            }
        }
    };


    class initVideosThread extends Thread {
        @Override
        public void run() {
            super.run();
            AbstructProvider provider = new VideoProvider(activity);
            List<Video> list = (List<Video>) provider.getList();

            List<Video> templist = new ArrayList<>();
            AllList = new HashMap<>();

            //我需要可以查看所有视频 所以加了这样一个文件夹名称
            AllList.put(" " + getResources().getString(R.string.all_video), list);

            //主要是读取文件夹的名称 做分文件夹的展示

            for (Video video : list) {
                String album = video.getAlbum();
                if (TextUtils.isEmpty(album)) {
                    album = "Camera";
                }

                if (AllList.containsKey(album)) {
                    AllList.get(album).add(video);
                } else {
                    templist = new ArrayList<>();
                    templist.add(video);
                    AllList.put(album, templist);
                }
            }

            //在子线程读取好数据后使用handler 更新
            if (list == null || list.size() == 0) {
                Message message = new Message();
                message.what = AppConstant.WHAT.FAILURE;
                mHandler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = AppConstant.WHAT.SUCCESS;
                message.obj = list;
                mHandler.sendMessage(message);
            }
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    protected void startRefreshing(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    protected void stopRefreshing(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    class Adapter extends BaseQuickAdapter<Video> {


        public Adapter(int layoutResId, List<Video> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Video item) {
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(item.getDuration()),
                    TimeUnit.MILLISECONDS.toMinutes(item.getDuration()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(item.getDuration())),
                    TimeUnit.MILLISECONDS.toSeconds(item.getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(item.getDuration())));

            helper.setText(R.id.text_duration, hms);
            ImageView simpleDraweeView = AdapterUtils.getAdapterView(helper.getConvertView(), R.id.simpleDraweeView);
            int width = (MyApplication.getInstance().getScreenWidth() - 4) / 4;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
            simpleDraweeView.setLayoutParams(layoutParams);

            Glide
                    .with(context)
                    .load(Uri.fromFile(new File(item.getPath())))
                    .asBitmap()
                    .into(simpleDraweeView);
        }
    }
}
