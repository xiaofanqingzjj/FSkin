package com.tencent.fskin.demo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.fskin.demo.base.adapter.quickAdapter
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.rv_item_article.view.*

class HomeFragment : Fragment() {



    class Data(var title: String? = null, var desc: String? = null)


    val datas = mutableListOf<Data>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testData()



        recycler_view.quickAdapter(
                data = datas,
                itemLayoutId = R.layout.rv_item_article,
                bindData = {_, data, itemView ->
                    itemView?.run {
                        title.text = data.title
                        desc.text = data.desc

                        setOnClickListener {
                            startActivity(Intent(it.context, ArticleDetailActivity::class.java))
                        }
                    }
                }
        )
    }

    private fun testData() {
        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })

        datas.add(Data().apply {
            title = "123 万人同时在线:直播答题小程序的「撒币」狂欢与「造号」热潮"
            desc = "知乎日报的公众号与头脑王者小程序进行绑定,吸引公众号读者参与答题,头脑王者小程序也在页面上为知乎热榜小程序预设了接口,通过小程序跳小程序的方式,为知乎热榜带... 百度快照"
        })
    }

}