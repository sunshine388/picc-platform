import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getPatientList(params) {
  return request({
    url: RESTURL.patients.getPatientList,
    method: 'get',
    params
  });
}

export function getPatientById(params) {
  return request({
    url: `${RESTURL.patients.getPatientById}/${params.id}`,
    method: 'get',
    params
  });
}

export function editPatient(params) {
  return request({
    url: `${RESTURL.patients.editPatient}/${params.id}`,
    method: 'put',
    params
  });
}

export function deletePatient(params) {
  return request({
    url: `${RESTURL.patients.deletePatient}/${params.id}`,
    method: 'delte',
    params
  });
}
