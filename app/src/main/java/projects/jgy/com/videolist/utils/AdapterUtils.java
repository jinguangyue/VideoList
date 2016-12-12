package projects.jgy.com.videolist.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Wxcily on 15/10/29.
 * Adapter ViewHolder 的简单写法
 */
public class AdapterUtils {
    private AdapterUtils() {
        //私有化构造方法防止实例化
    }

    /**
     * 用于自定义adapter if (convertView == null) { convertView
     * =LayoutInflater.from(context).inflate(R.layout.layout_item,parent,
     * false); } ImageView imageView =
     * getAdapterView(convertView,R.id.imageView);
     *
     * @param convertView
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T getAdapterView(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
