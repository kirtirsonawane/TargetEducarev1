package com.targeteducare.Classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryModel implements Serializable {

/*        "": "3",
                "": "XYZ",
                "": "क्ष",
                "": "0",*/

    String Id;
    String Name;
    String NameInMarathi;
    String ParentId;
    ArrayList<SubCategoryModel> subCategoryModels;


    public CategoryModel(String id, String name, String nameInMarathi, String parentId, ArrayList<SubCategoryModel> subCategoryModels) {
        this.Id = id;
        this.Name = name;
        this.NameInMarathi = nameInMarathi;
        this.ParentId = parentId;
        this.subCategoryModels = subCategoryModels;
    }

    public CategoryModel() {
    }

    public static ArrayList<CategoryModel> getjson(JSONObject jsonObject3) {
        ArrayList<CategoryModel> modelArrayList = new ArrayList<CategoryModel>();

        try {
            JSONArray jsonArray1 = null;

            jsonArray1 = jsonObject3.optJSONArray("subroot");
            if (jsonArray1 != null) {

                for (int i = 0; i < jsonArray1.length(); i++) {
                    CategoryModel categoryModel = new CategoryModel();
                    JSONObject object = jsonArray1.optJSONObject(i);
                    categoryModel.setId(object.optString("Id"));
                    categoryModel.setName(object.optString("Name"));
                    categoryModel.setNameInMarathi(object.optString("NameInMarathi"));
                    categoryModel.setParentId(object.optString("ParentId"));
                    ArrayList<SubCategoryModel> subCategoryModels = new ArrayList<>();
                    if (object.opt("SubCategory") != null) {
                        JSONArray jsonArray = object.optJSONArray("SubCategory");


                        if(jsonArray!=null) {
                            JSONArray jsonArray2=object.getJSONArray("SubCategory");
                            for (int j = 0; j <jsonArray2.length(); j++) {
                                JSONObject object1 = jsonArray2.getJSONObject(j);
                                //  SubCategoryModel subCategoryModel = new SubCategoryModel();
                                subCategoryModels.add(SubCategoryModel.getjsonforsubcategory(object1));
                            }

                    } else if (jsonArray==null){
                            if (object.optString("SubCategory") != null) {
                                subCategoryModels.add(SubCategoryModel.getjsonforsubcategory(object.getJSONObject("SubCategory")));
                            }
                            else {

                                    categoryModel.setSubCategoryModels(subCategoryModels);
                                }
                            }




                    }
                    categoryModel.setSubCategoryModels(subCategoryModels);

                    modelArrayList.add(categoryModel);

                }



            }


            else {
                JSONArray array1 = new JSONArray();

             /*   for (int i = 0; i < jsonArray1.length(); i++) {
                    array1 = jsonArray1.getJSONArray(i);
                    JSONObject object = array1.getJSONObject(i);
                    Log.e("json obj ", "subcategory " + object.get("Id"));

                }*/


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return modelArrayList;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getNameInMarathi() {
        return NameInMarathi;
    }

    public void setNameInMarathi(String nameInMarathi) {
        this.NameInMarathi = nameInMarathi;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        this.ParentId = parentId;
    }

    public ArrayList<SubCategoryModel> getSubCategoryModels() {
        return subCategoryModels;
    }

    public void setSubCategoryModels(ArrayList<SubCategoryModel> subCategoryModels) {
        this.subCategoryModels = subCategoryModels;
    }

   /* String style = "<style type=\"text/css\">\n" +
            "@font-face {\n" +
            "    font-family: Symbol;\n" +
            "    src:url(\"file:///android_asset/fonts/symbol.ttf\"), url(\"file:///android_asset/fonts/symbol_webfont.woff\"), url(\"file:///android_asset/fonts/symbol_webfont.woff2\");\n" +
            "}\n" +
            "@font-face {\n" +
            "    font-family: Calibri;\n" +
            "    src: url(\"file:///android_asset/fonts/calibri.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: AkrutiDevPriya;\n" +
            "    src: url(\"file:///android_asset/fonts/akrutidevpriyanormal.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: Times New Roman,serif;\n" +
            "    src: url(\"file:///android_asset/fonts/timesnewroman.ttf\")\n" +
            "}\n" +
            "\n" +
            "</style>";*/
}
