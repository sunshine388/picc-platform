import CryptoJS from 'crypto-js';
export default {
  encryptByDES(data, key) {
    key = key || '\u0067\u0072\u0065\u0061\u0074\u006d\u0061\u0070';
    var keyHex = CryptoJS.enc.Utf8.parse(key);
    var encrypted = CryptoJS.DES.encrypt(data, keyHex, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
  },
  decryptByDES(data, key) {
    key = key || '\u0067\u0072\u0065\u0061\u0074\u006d\u0061\u0070';
    let keyHex = CryptoJS.enc.Utf8.parse(key);
    let decrypted = CryptoJS.DES.decrypt(
      {
        ciphertext: CryptoJS.enc.Base64.parse(data)
      },
      keyHex,
      {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
      }
    );
    return decrypted.toString(CryptoJS.enc.Utf8);
  }
};
