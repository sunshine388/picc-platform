import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function requseLogin(data) {
  return request({
    url: RESTURL.manager.requseLogin,
    method: 'post',
    data
  });
}

export function getMenu(params) {
  return request({
    url: RESTURL.manager.getMenu,
    method: 'get',
    params
  });
}

export function getManagerList(params) {
  return request({
    url: RESTURL.manager.getManagerList,
    method: 'get',
    params
  });
}

export function changeManagerState(data) {
  return request({
    url: `${RESTURL.manager.changeManagerState}/${data.id}/${data.state}`,
    method: 'put',
    data
  });
}

export function addManager(data) {
  return request({
    url: RESTURL.manager.addManager,
    method: 'post',
    data
  });
}

export function getManagerById(params) {
  return request({
    url: `${RESTURL.manager.getManagerById}/${params.id}`,
    method: 'get',
    params
  });
}

export function editManager(params) {
  return request({
    url: `${RESTURL.manager.editManager}/${params.id}`,
    method: 'put',
    params
  });
}

export function updateManagerPassword(data) {
  return request({
    url: RESTURL.manager.updateManagerPassword,
    method: 'post',
    data
  });
}

export function deleteManager(params) {
  return request({
    url: `${RESTURL.manager.deleteManager}/${params.id}`,
    method: 'delete',
    params
  });
}
