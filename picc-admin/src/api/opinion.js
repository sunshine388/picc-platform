import request from '@/utils/request';
import RESTURL from '@/config/rest-url';

export function getOpinion(params) {
  return request({
    url: RESTURL.opinion.getOpinion,
    method: 'get',
    params
  });
}

export function updateOpinion(params) {
  return request({
    url: `${RESTURL.opinion.updateOpinion}/${params.id}/${params.solution}`,
    method: 'put',
    params
  });
}

export function changeOpinionState(params) {
  return request({
    url: `${RESTURL.opinion.changeOpinionState}/${params.id}/${params.whether_to_solve}`,
    method: 'put',
    params
  });
}
