import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getDepartment(params) {
  return request({
    url: RESTURL.common.getDepartment,
    method: 'get',
    params
  });
}
