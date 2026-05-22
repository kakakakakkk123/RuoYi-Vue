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

export function resetStudentPasswords(userIds, password) {
  return request({
    url: '/account/students/password',
    method: 'put',
    data: { roleIds: userIds, password }
  })
}

export function changeStudentStatus(userId, status) {
  return request({
    url: '/account/students/' + userId + '/status',
    method: 'put',
    data: { status }
  })
}

export function changeStudentStatuses(userIds, status) {
  return request({
    url: '/account/students/status',
    method: 'put',
    data: { roleIds: userIds, status }
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

export function getSecuritySettings() {
  return request({
    url: '/account/securitySettings',
    method: 'get'
  })
}

export function updateSecuritySettings(data) {
  return request({
    url: '/account/securitySettings',
    method: 'put',
    data
  })
}

export function importStudents(data) {
  return request({
    url: '/account/students/importData',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    method: 'post',
    data
  })
}

export function getStudentImportTemplate() {
  return request({
    url: '/account/students/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
