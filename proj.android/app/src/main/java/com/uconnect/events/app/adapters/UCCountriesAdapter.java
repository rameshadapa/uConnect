//*************************************************************************************************
//*************************************************************************************************
//      Project name                    	    : Alesco Happy
//      Class Name                              : CategoriesListAdapter
//      Author                                  : [x]cube LABS
//*************************************************************************************************
//*************************************************************************************************
package com.uconnect.events.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alesco.suggestionsapp.R;
import com.alesco.suggestionsapp.utils.SparseMap;

import java.util.ArrayList;

/**
 * CategoriesListAdapter is an Adapter class used to display the list of categories in
 * Recommendations module to filter Recommendations based on selected categories.
 */
public class UCCountriesAdapter extends BaseAdapter {


    private final SparseMap<Integer, String> categoriesList;
    private final ArrayList<String> selectedCategoryIds;
    private LayoutInflater inflater = null;

    public UCCountriesAdapter(Context context, SparseMap<Integer, String> categoriesList, ArrayList<String> selectedCategories) {
        this.categoriesList = categoriesList;
        this.selectedCategoryIds = selectedCategories;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoriesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.inflate_row_category, parent, false);
            viewHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
            viewHolder.categorySelectedImage = (ImageView) convertView.findViewById(R.id.category_selector_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int keyAtPosition = categoriesList.getKey(position);
        viewHolder.categoryName.setText(categoriesList.get(position));
        // Displaying the check mark image based on selected categories
        if (selectedCategoryIds.contains(String.valueOf(keyAtPosition))) {
            viewHolder.categorySelectedImage.setBackgroundResource(R.drawable.check_mark_active_icon);
        } else {
            viewHolder.categorySelectedImage.setBackgroundResource(R.drawable.check_mark_icon);
        }

        return convertView;
    }

    class ViewHolder {
        TextView categoryName;
        ImageView categorySelectedImage;
    }

}
