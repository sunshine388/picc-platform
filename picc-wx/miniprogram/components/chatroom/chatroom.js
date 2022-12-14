const FATAL_REBUILD_TOLERANCE = 10
const SETDATA_SCROLL_TO_BOTTOM = {
  scrollTop: 100000,
  scrollWithAnimation: true,
}

Component({
  properties: {
    envId: String, // chatRoomEnvId: 'release-f8415a',
    groupId: String, //demo 聊天室唯一id
    otherNumber: String,
    myNumber: String,

    onGetUserInfo: {
      type: Function,
    },
    getOpenID: {
      type: Function,
    },
  },

  data: {
    chats: [],
    textInputValue: '',
    openId: '',
    scrollTop: 0,
    scrollToMessage: '',
    hasKeyboard: false,
    collection: 'chatroom', //chatroom 数据库集合名字
    myName: '',
    myAvatarUrl: '',
    otherName: '',
    otherAvatarUrl: ''
  },

  methods: {
    onGetUserInfo(e) {
      this.properties.onGetUserInfo(e)
    },

    getOpenID() {
      return this.properties.getOpenID()
    },

    mergeCommonCriteria(criteria) {
      return {
        groupId: this.data.groupId,
        ...criteria,
      }
    },
    async getAvatarUrlandName() {
      const db = wx.cloud.database(); //引用数据库
      const _ = db.command
      db.collection('userInfo').doc(this.data.myNumber).get({
        success: (res) => {
          this.setData({
            myName: res.data.username,
            myAvatarUrl: res.data.avatarUrl
          })
        }
      })
      db.collection('userInfo').doc(this.data.otherNumber).get({
        success: (res) => {
          this.setData({
            otherName: res.data.username,
            otherAvatarUrl: res.data.avatarUrl
          })
        }
      })
    },
    async initRoom() {
      this.try(async () => {
        await this.initOpenID();
        await this.getAvatarUrlandName();
        const collection = this.data.collection;
        this.db = wx.cloud.database()
        const db = this.db
        const _ = db.command

        const {
          data: initList
        } = await db.collection(collection).where({
          groupId: _.eq(this.data.groupId)
        }).orderBy('sendTimeTS', 'desc').get()

        console.log('init query chats', initList)

        this.setData({
          chats: initList.reverse(),
          scrollTop: 10000,
        })

        this.initWatch(initList.length ? {
          sendTimeTS: _.gt(initList[initList.length - 1].sendTimeTS),
        } : {})
      }, '初始化失败')
    },

    async initOpenID() {
      return this.try(async () => {
        const openId = await this.getOpenID()

        this.setData({
          openId,
        })
      }, '初始化 openId 失败')
    },

    async initWatch(criteria) {
      this.try(() => {
        const collection = this.data.collection;
        const db = this.db
        const _ = db.command

        console.warn(`开始监听`, criteria)
        this.messageListener = db.collection(collection).where(this.mergeCommonCriteria(criteria)).watch({
          onChange: this.onRealtimeMessageSnapshot.bind(this),
          onError: e => {
            if (!this.inited || this.fatalRebuildCount >= FATAL_REBUILD_TOLERANCE) {
              this.showError(this.inited ? '监听错误，已断开' : '初始化监听失败', e, '重连', () => {
                this.initWatch(this.data.chats.length ? {
                  sendTimeTS: _.gt(this.data.chats[this.data.chats.length - 1].sendTimeTS),
                } : {})
              })
            } else {
              this.initWatch(this.data.chats.length ? {
                sendTimeTS: _.gt(this.data.chats[this.data.chats.length - 1].sendTimeTS),
              } : {})
            }
          },
        })
      }, '初始化监听失败')
    },

    onRealtimeMessageSnapshot(snapshot) {
      console.warn(`收到消息`, snapshot)

      if (snapshot.type === 'init') {
        this.setData({
          chats: [
            ...this.data.chats,
            ...[...snapshot.docs].sort((x, y) => x.sendTimeTS - y.sendTimeTS),
          ],
        })
        this.scrollToBottom()
        this.inited = true
      } else {
        let hasNewMessage = false
        let hasOthersMessage = false
        const chats = [...this.data.chats]
        for (const docChange of snapshot.docChanges) {
          switch (docChange.queueType) {
            case 'enqueue': {
              hasOthersMessage = docChange.doc._openid !== this.data.openId
              const ind = chats.findIndex(chat => chat._id === docChange.doc._id)
              if (ind > -1) {
                if (chats[ind].msgType === 'image' && chats[ind].tempFilePath) {
                  chats.splice(ind, 1, {
                    ...docChange.doc,
                    tempFilePath: chats[ind].tempFilePath,
                  })
                } else chats.splice(ind, 1, docChange.doc)
              } else {
                hasNewMessage = true
                chats.push(docChange.doc)
              }
              break
            }
          }
        }
        this.setData({
          chats: chats.sort((x, y) => x.sendTimeTS - y.sendTimeTS),
        })
        if (hasOthersMessage || hasNewMessage) {
          this.scrollToBottom()
        }
      }
    },

    async onConfirmSendText(e) {
      this.try(async () => {
        if (!e.detail.value) {
          return
        }

        const db1 = wx.cloud.database(); //引用数据库
        const id1 = this.data.otherNumber + this.data.myNumber;
        const id2 = this.data.myNumber + this.data.otherNumber;
        //更新云数据库与对方最后一次聊天记录
        const lastMessage1 = {
          groupId: this.data.groupId,
          msgType: 'text',
          textContent: e.detail.value,
          sendTime: new Date(),
          sendTimeTS: Date.now(), // fallback
          myNumber: this.data.myNumber,
          otherNumber:this.data.otherNumber,
          name: this.data.otherName,
          avatarUrl:this.data.otherAvatarUrl,
        }
        db1.collection('lastMessage').doc(id1).set({
          data: lastMessage1
        })
        //更新云数据库对方的最后一次聊天记录
        const lastMessage2 = {
          groupId: this.data.groupId,
          msgType: 'text',
          textContent: e.detail.value,
          sendTime: new Date(),
          sendTimeTS: Date.now(), // fallback
          myNumber: this.data.otherNumber,
          otherNumber:this.data.myNumber,
          name: this.data.myName,
          avatarUrl:this.data.myAvatarUrl
        }
        db1.collection('lastMessage').doc(id2).set({
          data: lastMessage2
        })

        const collection = this.data.collection;
        const db = this.db
        const _ = db.command
        const doc = {
          _id: `${Math.random()}_${Date.now()}`,
          groupId: this.data.groupId,
          msgType: 'text',
          textContent: e.detail.value,
          sendTime: new Date(),
          sendTimeTS: Date.now(), // fallback
          otherNumber: this.data.otherNumber,
          myNumber: this.data.myNumber,
        }
        this.setData({
          textInputValue: '',
          chats: [
            ...this.data.chats,
            {
              ...doc,
              _openid: this.data.openId,
              writeStatus: 'pending',
            },
          ],
        })
        this.scrollToBottom(true)
        await db.collection(collection).add({
          data: doc,
        })

        this.setData({
          chats: this.data.chats.map(chat => {
            if (chat._id === doc._id) {
              return {
                ...chat,
                writeStatus: 'written',
              }
            } else return chat
          }),
        })
      }, '发送文字失败')
    },

    async onChooseImage(e) {
      wx.chooseImage({
        count: 1,
        sourceType: ['album', 'camera'],
        success: async res => {
          const db1 = wx.cloud.database(); //引用数据库
          const id1 = this.data.otherNumber + this.data.myNumber;
          const id2 = this.data.myNumber + this.data.otherNumber;
          //更新云数据库与对方最后一次聊天记录
          const lastMessage1 = {
            groupId: this.data.groupId,
            msgType: 'text',
            textContent: '[图片]',
            sendTime: new Date(),
            sendTimeTS: Date.now(), // fallback
            myNumber: this.data.myNumber,
            otherNumber:this.data.otherNumber,
            name: this.data.otherName,
            avatarUrl:this.data.otherAvatarUrl
          }
          db1.collection('lastMessage').doc(id1).set({
            data: lastMessage1
          }) //更新云数据库对方的最后一次聊天记录
          const lastMessage2 = {
            groupId: this.data.groupId,
            msgType: 'text',
            textContent: '[图片]',
            sendTime: new Date(),
            sendTimeTS: Date.now(), // fallback
            myNumber: this.data.otherNumber,
            otherNumber:this.data.myNumber,
            name: this.data.myName,
            avatarUrl:this.data.myAvatarUrl
          }
          db1.collection('lastMessage').doc(id2).set({
            data: lastMessage2
          })

          const collection = this.data.collection;
          const doc = {
            _id: `${Math.random()}_${Date.now()}`,
            groupId: this.data.groupId,
            msgType: 'image',
            sendTime: new Date(),
            sendTimeTS: Date.now(), // fallback
            otherNumber: this.data.otherNumber,
            myNumber: this.data.myNumber,
          }

          this.setData({
            chats: [
              ...this.data.chats,
              {
                ...doc,
                _openid: this.data.openId,
                tempFilePath: res.tempFilePaths[0],
                writeStatus: 0,
              },
            ]
          })
          this.scrollToBottom(true)

          const uploadTask = wx.cloud.uploadFile({
            cloudPath: `${this.data.openId}/${Math.random()}_${Date.now()}.${res.tempFilePaths[0].match(/\.(\w+)$/)[1]}`,
            filePath: res.tempFilePaths[0],
            success: res => {
              this.try(async () => {
                await this.db.collection(collection).add({
                  data: {
                    ...doc,
                    imgFileID: res.fileID,
                  },
                })
              }, '发送图片失败')
            },
            fail: e => {
              this.showError('发送图片失败', e)
            },
          })

          uploadTask.onProgressUpdate(({
            progress
          }) => {
            this.setData({
              chats: this.data.chats.map(chat => {
                if (chat._id === doc._id) {
                  return {
                    ...chat,
                    writeStatus: progress,
                  }
                } else return chat
              })
            })
          })
        },
      })
    },

    onMessageImageTap(e) {
      wx.previewImage({
        urls: [e.target.dataset.fileid],
      })
    },

    scrollToBottom(force) {
      if (force) {
        console.log('force scroll to bottom')
        this.setData(SETDATA_SCROLL_TO_BOTTOM)
        return
      }

      this.createSelectorQuery().select('.body').boundingClientRect(bodyRect => {
        this.createSelectorQuery().select(`.body`).scrollOffset(scroll => {
          if (scroll.scrollTop + bodyRect.height * 3 > scroll.scrollHeight) {
            console.log('should scroll to bottom')
            this.setData(SETDATA_SCROLL_TO_BOTTOM)
          }
        }).exec()
      }).exec()
    },

    async onScrollToUpper() {
      if (this.db && this.data.chats.length) {
        const collection = this.data.collection;
        const _ = this.db.command
        const {
          data
        } = await this.db.collection(collection).where(this.mergeCommonCriteria({
          sendTimeTS: _.lt(this.data.chats[0].sendTimeTS),
        })).orderBy('sendTimeTS', 'desc').get()
        this.data.chats.unshift(...data.reverse())
        this.setData({
          chats: this.data.chats,
          scrollToMessage: `item-${data.length}`,
          scrollWithAnimation: false,
        })
      }
    },

    async try (fn, title) {
      try {
        await fn()
      } catch (e) {
        this.showError(title, e)
      }
    },

    showError(title, content, confirmText, confirmCallback) {
      console.error(title, content)
      wx.showModal({
        title,
        content: content.toString(),
        showCancel: confirmText ? true : false,
        confirmText,
        success: res => {
          res.confirm && confirmCallback()
        },
      })
    },
  },

  ready() {
    global.chatroom = this
    this.initRoom()
    this.fatalRebuildCount = 0
  },
})