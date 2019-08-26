package br.com.githubjavapop.models;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class RepositoryDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<RepositoryDataSource> mutableLiveData;
    private RepositoryDataSource dataSource;

    public RepositoryDataSourceFactory() {
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {

        dataSource = new RepositoryDataSource();
        mutableLiveData.postValue(dataSource);
        return dataSource;
    }
}
