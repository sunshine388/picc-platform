import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getBindingList(params) {
  return request({
    url: RESTURL.binding.getBindingList,
    method: 'get',
    params
  });
}

export function getBindingById(params) {
  return request({
    url: `${RESTURL.binding.getBindingById}/${params.id}`,
    method: 'get',
    params
  });
}

export function editBinding(params) {
  return request({
    url: `${RESTURL.binding.editBinding}/${params.id}`,
    method: 'put',
    params
  });
}

export function deleteBinding(params) {
  return request({
    url: `${RESTURL.binding.deleteBinding}/${params.id}`,
    method: 'delete',
    params
  });
}
