import request from '@/utils/request'

export function listStudents(query) {
  return request({
    url: '/account/students',
    method: 'get',
    params: query
  })
}

export function getStudent(userId) {
  return request({
    url: '/account/students/' + userId,
    method: 'get'
  })
}

export function resetStudentPassword(userId, password) {
  return request({
    url: '/account/students/' + userId + '/password',
    method: 'put',
    data: { password }
  })
}

export function changeStudentStatus(userId, status) {
  return request({
    url: '/account/students/' + userId + '/status',
    method: 'put',
    data: { status }
  })
}

export function deleteStudents(userIds) {
  return request({
    url: '/account/students/' + userIds,
    method: 'delete'
  })
}

export function getRegisterEnabled() {
  return request({
    url: '/account/registerEnabled',
    method: 'get'
  })
}

export function setRegisterEnabled(status) {
  return request({
    url: '/account/registerEnabled',
    method: 'put',
    data: { status }
  })
}
