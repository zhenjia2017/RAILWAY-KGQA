// pages/search/search.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        isSearch: true,
        isClear: false,
        inputValue: "",
        scriptValue: "",
        infoList: [],
        trainNoList: [],
        stationList: [],
        currentType: "",
        checkUpdateList: false,
        analysisSeg: [],
        analysisRec: [],
        analysisLink: [],
        analysisType: 0,
    },

    getInput: function (e) {
        this.setData({
            inputValue: e.detail.value
        })
        if (this.data.inputValue.length > 0) {
            this.setData({
                isSearch: false,
                isClear: true,
            })
        } else {
            this.setData({
                isSearch: true,
                isClear: false,
            })
        }
    },


    clearTap: function () {
        this.setData({
            inputValue: '',
            isSearch: true,
            isClear: false,
        })
    },


    searchTap: function () {
        var self = this
        // 清零
        this.setData({
            scriptValue: "",
            infoList: [],
            trainNoList: [],
            stationList: [],
            checkUpdateList: false,
            analysisSeg: [],
            analysisRec: [],
            analysisLink: [],
            analysisType: 0,
        })

        // 问题解析
        wx.request({
            url: 'http://192.168.123.213:8080/kgqa/query',
            header: { //请求头
                "Content-Type": "application/json"
            },
            method: 'GET',
            data: { //发送给后台的数据
                question: self.data.inputValue
            },
            success: function (res) {
                var json = res.data
                console.log(json)

                if (json.answerType == "train_no") {
                    self.setData({
                        currentType: "train_no",
                        trainNoList: json.answer,
                    })
                } else if (json.answerType == "station") {
                    self.setData({
                        currentType: "station",
                        stationList: json.answer,
                    })
                }
            },
            fail: function (err) { //请求失败
                console.log(err)
            },
            complete: function () { //请求完成后执行的函数
                // 1.问题分词结果
                wx.request({
                    url: 'http://192.168.123.213:8080/kgqa/query/analysis/seg',
                    header: {
                        "Content-Type": "application/json"
                    },
                    method: 'GET',
                    success: function (res) {
                        self.setData({
                            analysisSeg: res.data,
                        });
                    },
                    fail: function (err) { //请求失败
                        console.log(err);
                    }
                })

                // 2.实体识别结果
                wx.request({
                    url: 'http://192.168.123.213:8080/kgqa/query/analysis/rec',
                    header: {
                        "Content-Type": "application/json"
                    },
                    method: 'GET',
                    success: function (res) {
                        self.setData({
                            analysisRec: res.data,
                        });
                    },
                    fail: function (err) { //请求失败
                        console.log(err);
                    }
                })

                // 3.实体链接结果
                wx.request({
                    url: 'http://192.168.123.213:8080/kgqa/query/analysis/link',
                    header: {
                        "Content-Type": "application/json"
                    },
                    method: 'GET',
                    success: function (res) {
                        self.setData({
                            analysisLink: res.data,
                        });
                    },
                    fail: function (err) {
                        console.log(err);
                    }
                })

                // 4.问题分类结果
                wx.request({
                    url: 'http://192.168.123.213:8080/kgqa/query/analysis/type',
                    header: {
                        "Content-Type": "application/json"
                    },
                    method: 'GET',
                    success: function (res) {
                        console.log(res.data)
                        self.setData({
                            analysisType: res.data,
                        });
                    },
                    fail: function (err) {
                        console.log(err);
                    }
                })

                self.setData({
                    checkUpdateList: true
                })

                // 查询结果进行二次查询，返回详细信息
                if (self.data.currentType == "train_no") {
                    console.log("执行条件判断")
                    // 答案类型是车次号，遍历每一个车次号train_no

                    var list = []
                    for (var i = 0; i < self.data.trainNoList.length; i++) {
                        console.log("执行for循环")
                        wx.request({
                            url: 'http://192.168.123.213:8080/kgqa/findOneTrainNo',
                            header: { //请求头
                                "Content-Type": "application/json"
                            },
                            method: 'GET',
                            data: { //发送给后台的数据
                                trainNo: self.data.trainNoList[i]
                            },
                            success: function (res) {
                                var json = JSON.stringify(res.data)
                                console.log(json)
                                // console.log(json); // 打印返回数据
                                //res.data相当于ajax里面的data,为后台返回的数据
                                //如果在success直接写this就变成了wx.request()的this了
                                //必须为getdata函数的this,不然无法重置调用函数
                                for (var i = 0; i < res.data.length; i++) {
                                    var obj = {
                                        trainNo: res.data[i]['trainNo'],
                                        stationFrom: res.data[i]['stationFrom'],
                                        timeArriveFrom: res.data[i]['timeArriveFrom'],
                                        timeDepartureFrom: res.data[i]['timeDepartureFrom'],
                                        timeTravelFrom: res.data[i]['timeTravelFrom'],
                                        distanceTravelFrom: res.data[i]['distanceTravelFrom'],
                                        seqFrom: res.data[i]['seqFrom'],
                                        isTerminalFrom: res.data[i]['isTerminalFrom'],
                                        stationTo: res.data[i]['stationTo'],
                                        timeArriveTo: res.data[i]['timeArriveTo'],
                                        distanceTravelTo: res.data[i]['distanceTravelTo'],
                                        timeDepartureTo: res.data[i]['timeDepartureTo'],
                                        timeTravelTo: res.data[i]['timeTravelTo'],
                                        seqTo: res.data[i]['seqTo'],
                                        isTerminalTo: res.data[i]['isTerminalTo'],
                                    }


                                    list.push(obj);
                                }
                                self.setData({
                                    infoList: list,
                                });
                                console.log(self.data.infoList.toString)
                            },
                            fail: function (err) { //请求失败
                                console.log(err);
                            },
                            complete: function () { //请求完成后执行的函数
                                
                            }
                        })
                    }
                }
                if (self.data.currentType == "station") {
                    console.log("执行条件判断")
                    // 答案类型是车次号，遍历每一个车次号train_no
                    // var list = []
                    // for (var i = 0; i < self.data.stationList.length; i++) {
                    //     var obj = {
                    //         station: self.data.stationList[i]
                    //     }
                    //     console.log(obj)
                    //     list.push(obj);
                    // }
                    self.setData({
                        infoList: self.data.stationList
                    })
                }
            }
        })


    },


    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})