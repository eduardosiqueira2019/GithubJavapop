package br.com.githubjavapop.models;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;

public class RepositoryDataSource extends PageKeyedDataSource<Integer, Repository> {

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Repository> callback) {
        //show progress bar
        AndroidNetworking.get("https://api.github.com/search/repositories?q=language:Java&sort=stars&page={pageNumber}")
                .addPathParameter("pageNumber", "0")
                //.addQueryParameter("limit", "3")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("total_count")) {
                            ArrayList<Repository> repositoryArrayList;
                            repositoryArrayList = createRepoList(response);
                            //MainActivityViewModel.repositoryPagedList.getValue();
                            callback.onResult(repositoryArrayList, 1, 2);
                            //repositoryArrayList.clear();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Repository> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Repository> callback) {
        AndroidNetworking.get("https://api.github.com/search/repositories?q=language:Java&sort=stars&page={pageNumber}")
                .addPathParameter("pageNumber", String.valueOf(params.key))
                //.addQueryParameter("limit", "3")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("total_count")) {
                            ArrayList<Repository> repositoryArrayList;
                            repositoryArrayList = createRepoList(response);
                            //MainActivityViewModel.repositoryPagedList.getValue();
                            callback.onResult(repositoryArrayList, params.key + 1);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private ArrayList<Repository> createRepoList(JSONObject response) {

        ArrayList<Repository> repoList = new ArrayList<>();
        Repository repository;

        //epository = Repository.fromJson(response.getJSONArray("items"));
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                repository = new Repository(jsonArray.getJSONObject(i));
                repoList.add(repository);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // send back to PagedList handler
        //callbackonResult(tweets);
        return repoList;

    }

}
