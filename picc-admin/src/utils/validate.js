const validate = {
  // 密码  6-20位字母，数字和符号两种以上组合
  password: {
    validator: (rule, value, callback) => {
      if (!value) {
        callback();
      }
      const reg =
        /((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))(?!^.*[\u4E00-\u9FA5].*$)^\S{6,16}$/;
      //const reg = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]).{8,16}$/i;
      if (reg.test(value)) {
        callback();
      } else {
        return callback(new Error('6-16位字母，数字和符号两种以上组合'));
      }
    }
  },
  //不同密码
  oldPassword: (value) => {
    let _value = value;
    return {
      validator: (rule, value, callback) => {
        if (!value || !_value) {
          callback();
        }
        if (_value !== value) {
          callback();
        } else {
          return callback(new Error('新密码不能与旧密码一致'));
        }
      },
      trigger: 'change'
    };
  },
  //相同密码
  samePassword: (value) => {
    let _value = value;
    return {
      validator: (rule, value, callback) => {
        if (!value || !_value) {
          callback();
        }
        if (_value === value) {
          callback();
        } else {
          return callback(new Error('两次输入密码不一致!'));
        }
      },
      trigger: 'change'
    };
  },
  // 必填
  required: {
    required: true,
    message: '必填',
    trigger: 'change'
  },
  // email
  email: {
    type: 'email',
    message: '请输入正确的邮箱'
  },
  // 非必填
  noRequired: {
    required: false,
    message: '非必填',
    trigger: 'change'
  },
  phone: {
    validator: (rule, value, callback) => {
      if (!value) {
        callback();
      }
      var re =
        /^((0\d{2,3}-\d{7,8})|((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8})$/g;
      if (re.test(value)) {
        callback();
      } else {
        return callback(new Error('必须为合法电话'));
      }
    }
  }
};

export default validate;
