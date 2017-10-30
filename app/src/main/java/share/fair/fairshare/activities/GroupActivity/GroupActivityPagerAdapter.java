package share.fair.fairshare.activities.GroupActivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niryo on 20/10/2017.
 */

   public class GroupActivityPagerAdapter extends PagerAdapter {
        private final List<View> viewList = new ArrayList<>();
        private final List<String> viewTitle = new ArrayList<>();
        private final Context context;

        public GroupActivityPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        collection.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    public void addView(View view, String title) {
            viewList.add(view);
            viewTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return viewTitle.get(position);
        }
    }

