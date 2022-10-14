import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getPatientsRecord(params) {
  return request({
    url: RESTURL.record.getPatientsRecord,
    method: 'get',
    params
  });
}

export function getPatientRecord(params) {
  return request({
    url: `${RESTURL.record.getPatientRecord}/${params.patientnum}`,
    method: 'get',
    params
  });
}

export function getPatientCatheterNew(params) {
  return request({
    url: `${RESTURL.record.getPatientCatheterNew}/${params.recordNum}`,
    method: 'get',
    params
  });
}

export function getPatientMaintainNew(params) {
  return request({
    url: `${RESTURL.record.getPatientMaintainNew}/${params.recordNum}`,
    method: 'get',
    params
  });
}

export function getPatientExtubationNew(params) {
  return request({
    url: `${RESTURL.record.getPatientExtubationNew}/${params.recordNum}`,
    method: 'get',
    params
  });
}

export function getPatientsCatheter(params) {
  return request({
    url: RESTURL.record.getPatientsCatheter,
    method: 'get',
    params
  });
}

export function getPatientCatheter(params) {
  return request({
    url: `${RESTURL.record.getPatientCatheter}/${params.patientnum}`,
    method: 'get',
    params
  });
}

export function getPatientsMaintain(params) {
  return request({
    url: RESTURL.record.getPatientsMaintain,
    method: 'get',
    params
  });
}

export function getPatientMaintain(params) {
  return request({
    url: `${RESTURL.record.getPatientMaintain}/${params.patientnum}`,
    method: 'get',
    params
  });
}

export function getPatientsExtubation(params) {
  return request({
    url: RESTURL.record.getPatientsExtubation,
    method: 'get',
    params
  });
}

export function getPatientExtubation(params) {
  return request({
    url: `${RESTURL.record.getPatientExtubation}/${params.patientnum}`,
    method: 'get',
    params
  });
}
