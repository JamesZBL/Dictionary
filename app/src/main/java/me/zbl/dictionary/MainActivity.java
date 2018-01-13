package me.zbl.dictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  //英译汉
  private Map<String, String> mMapEnToZh;
  //汉译英
  private Map<String, String> mMapZhToEn;

  private Button mBtnTranslate;
  private EditText mEdtSearch;
  private TextView mTvResult;
  private Spinner mSpinSearchMode;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    initListener();
    //生成词典
    initDictionary();
  }

  private void initView() {
    setTitle("小词典");
    mBtnTranslate = (Button) findViewById(R.id.id_btnSearch);
    mEdtSearch = (EditText) findViewById(R.id.id_edtSearch);
    mTvResult = (TextView) findViewById(R.id.id_tvRes);
    mSpinSearchMode = (Spinner) findViewById(R.id.id_spin_searchMode);
  }

  private void initListener() {
    mBtnTranslate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        search();
      }
    });
  }

  /**
   * 词典初始化
   */
  private void initDictionary() {
    mMapEnToZh = new HashMap<>();
    mMapZhToEn = new HashMap<>();
    try {
      //生成英译汉词典
      createEnToZhDictionary();
      //生成汉译英词典
      createZhToEnDictionary();
    } catch (IOException e) {
      e.printStackTrace();
      Log.e("异常", "解析词典一异常");
    }
  }

  /**
   * 查询结果
   */
  private void search() {
    int index = mSpinSearchMode.getSelectedItemPosition();
    switch (index) {
      case 0: {
        // 英译汉
        String searchString = mEdtSearch.getText().toString();
        String resultString = mMapEnToZh.get(searchString);
        if (null != resultString) {
          mTvResult.setText(resultString);
        } else {
          mTvResult.setText("没有查到结果");
        }
        break;
      }
      case 1: {
        // 汉译英
        // 英译汉
        String searchString = mEdtSearch.getText().toString();
        String resultString = mMapZhToEn.get(searchString);
        if (null != resultString) {
          mTvResult.setText(resultString);
        } else {
          mTvResult.setText("没有查到结果");
        }
        break;
      }
    }
  }

  /**
   * 创建英译汉词典
   *
   * @throws IOException 文件读取异常
   */
  private void createEnToZhDictionary() throws IOException {
    //词典文件流
    InputStream in = getAssets().open("word.txt");
    BufferedReader reader;
    String tempString;
    reader = new BufferedReader(new InputStreamReader(in));
    while (null != (tempString = reader.readLine())) {
      //读取一行文本，将这行文本以逗号进行分割，形成数组 strArr
      String[] strArr = tempString.split(",");
      //数组的第一个元素作为键，之后的各元素作值
      String keyString = strArr[0];
      String valueString = "";
      //将英文部分进行组合
      for (int i = 1; i < strArr.length; i++) {
        valueString += strArr[i];
        if (i != strArr.length - 1) {
          valueString += ",";
        }
      }
      //结果存入map中
      mMapEnToZh.put(keyString, valueString);
      Log.d("生成词典", "key:" + keyString + ",value:" + valueString);
    }
  }

  /**
   * 生成汉译英的词典
   *
   * @throws IOException 文件读取异常
   */
  private void createZhToEnDictionary() throws IOException {
    //词典文件流
    InputStream in = getAssets().open("word.txt");
    BufferedReader reader;
    String tempString;
    reader = new BufferedReader(new InputStreamReader(in));
    while (null != (tempString = reader.readLine())) {
      //读取一行文本，将这行文本以逗号进行分割，形成数组 strArr
      String[] strArr = tempString.split(",|;");
      //数组的第一个元素作为键，之后的各元素作值
      String wordString = strArr[0];
      //将每一个汉语意思作为一个键
      for (int i = 1; i < strArr.length; i++) {
        if (null != mMapZhToEn && null == mMapZhToEn.get(strArr[i])) {
          // 如果不存在相同的汉语词
          mMapZhToEn.put(strArr[i], wordString);
          Log.d("生成词典", "key:" + wordString + ",value:" + strArr[i]);
        } else if (null != mMapZhToEn) {
          // 如果存在相同的汉语词，将英文单词追加到后面
          StringBuilder builder = new StringBuilder();
          builder.append(mMapZhToEn.get(strArr[i]))
                  .append(",")
                  .append(wordString);
          mMapZhToEn.put(strArr[i], builder.toString());
        }
      }
    }
  }
}
