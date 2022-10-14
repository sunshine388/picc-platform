import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getSpecialistList(params) {
  return request({
    url: RESTURL.specialists.getSpecialistList,
    method: 'get',
    params
  });
}

export function changeSpecialistState(params) {
  return request({
    url: `${RESTURL.specialists.changeSpecialistState}/${params.id}/${params.state}`,
    method: 'put',
    params
  });
}

export function getSpecialistById(params) {
  return request({
    url: `${RESTURL.specialists.getSpecialistById}/${params.id}`,
    method: 'get',
    params
  });
}

export function editSpecialist(params) {
  return request({
    url: `${RESTURL.specialists.editSpecialist}/${params.id}`,
    method: 'put',
    params
  });
}

export function deleteSpecialist(params) {
  return request({
    url: `${RESTURL.specialists.deleteSpecialist}/${params.id}`,
    method: 'delete',
    params
  });
}
