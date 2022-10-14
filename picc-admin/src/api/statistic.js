import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getStatisticCatheter(params) {
  return request({
    url: RESTURL.statistic.getStatisticCatheter,
    method: 'get',
    params
  });
}

export function getStatisticMaintain(params) {
  return request({
    url: RESTURL.statistic.getStatisticMaintain,
    method: 'get',
    params
  });
}

export function getStatisticExtubation(params) {
  return request({
    url: RESTURL.statistic.getStatisticExtubation,
    method: 'get',
    params
  });
}

export function getStatisticCompanion(params) {
  return request({
    url: RESTURL.statistic.getStatisticCompanion,
    method: 'get',
    params
  });
}
