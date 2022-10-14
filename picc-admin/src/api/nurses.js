import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getNurseList(params) {
  return request({
    url: RESTURL.nurses.getNurseList,
    method: 'get',
    params
  });
}

export function changeNurseState(params) {
  return request({
    url: `${RESTURL.nurses.changeNurseState}/${params.id}/${params.state}`,
    method: 'put',
    params
  });
}

export function getNurseById(params) {
  return request({
    url: `${RESTURL.nurses.getNurseById}/${params.id}`,
    method: 'get',
    params
  });
}

export function editNurse(params) {
  return request({
    url: `${RESTURL.nurses.editNurse}/${params.id}`,
    method: 'put',
    params
  });
}

export function deleteNurse(params) {
  return request({
    url: RESTURL.nurses.deleteNurse,
    method: 'delete',
    params
  });
}

export function getScheduleByJobnumber(params) {
  return request({
    url: RESTURL.nurses.getScheduleByJobnumber,
    method: 'get',
    params
  });
}

export function postSchedule(data) {
  return request({
    url: RESTURL.nurses.postSchedule,
    method: 'post',
    data
  });
}

export function getSchedule(params) {
  return request({
    url: RESTURL.nurses.getSchedule,
    method: 'get',
    params
  });
}

export function deleteScheduleOne(data) {
  return request({
    url: RESTURL.nurses.deleteScheduleOne,
    method: 'post',
    data
  });
}

export function getScheduleByTime(params) {
  return request({
    url: RESTURL.nurses.getScheduleByTime,
    method: 'get',
    params
  });
}
